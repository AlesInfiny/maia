---
status: accepted
---

# Relocate routed views from per-context `views/` folders to a top-level `src/pages/`

Screen URLs should be designed around resources, not around which bounded context or domain owns the code, and a growing number of screens compose UI/logic across multiple contexts (see [[CONTEXT-MAP]]). To make the folder structure match that reality, each workspace's routed screens move out of `{context}/{domain}/views/*View.vue` into a top-level `src/pages/`, mirrored 1:1 to the app's existing URL path segments (dynamic segments like `:itemId` are not represented as folders), and are renamed `*Page.vue` (the term **View** is retired in favor of **Page** — see `CONTEXT-MAP.md`). Route definitions (path, `meta`, guards) stay where they are today, in each context's `router/*.ts` — only the `component:` import target changes to `@/pages/...`. Each context gains a `public-api.ts` at its root; `src/pages` and other contexts may only reach into a context through it, never through its internal `components/`/`services/`/`stores/`/`validation/` folders, and `eslint.project-rules.ts`'s `createLayerDependencyRules` is extended to put `pages` at the top of the existing layer hierarchy (`pages` may import a context's `public-api.ts`, `business-common`, `system-common`; nothing may import from `@/pages/**`).

## Considered Options

- **Vue Router's file-based "Pages Router" (`unplugin-vue-router`)**: technically feasible (Vite-based, `<script setup>` throughout, no nested routes, a single `meta.requiresAuth` key, small view count), but every `router.push`/`router.replace` call in both apps (8 in admin, 13 in consumer) and both auth guards navigate by name via hand-written `*-route-names.ts` constants. Adopting it now would mean reworking all of those call sites in the same change as the physical relocation. Deferred to a separate, later initiative once `src/pages` has stabilized.
- **Flat resource folders** (e.g. `src/pages/catalog/items/{ItemsPage,ItemsAddPage,ItemsEditPage}.vue`) vs. **URL-mirrored directory hierarchy**: chose the hierarchy (e.g. `src/pages/catalog/items/add/ItemsAddPage.vue`) even though Pages Router isn't adopted yet, so the folder structure documents the URL shape directly.
- **Free cross-context imports from `pages`** (treating `pages` like `App.vue`/`main.ts`, which are exempted from the layer rules) vs. **a per-context `public-api.ts` boundary**: chose the public-API boundary so cross-context composition from `pages` stays visible and reviewable, rather than reaching into arbitrary internals.

## Consequences

- Migration proceeds context by context, starting with a low-risk warm-up (`system-common`'s Home/Error/NotFound screens, flattened at the top of `src/pages`), then `admin/catalog-management` (exercises the `public-api.ts` pattern against its one existing cross-context reference, `assets-management`'s asset helper).
- `eslint.project-rules.ts` needs a new rule set for the `pages` layer before any context migrates, not after.
- Because Pages Router is deferred, route paths, names, and `meta` are unchanged for now — only the component file's location and name change.
