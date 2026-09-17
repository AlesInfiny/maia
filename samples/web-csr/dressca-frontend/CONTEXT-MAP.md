# Context Map

This repo (`dressca-frontend`) is an npm workspaces monorepo with two frontend applications, `admin` and `consumer`, each organized into bounded contexts under its own `src/`.

## Contexts

- **admin**
  - [Catalog Management](./admin/src/catalog-management/CONTEXT.md) — catalog item management for store operators
  - [Assets Management](./admin/src/assets-management/CONTEXT.md) — management of media assets (e.g. item images)
  - [Security](./admin/src/security/CONTEXT.md) — authentication/authorization for admin operators
- **consumer**
  - [Shopping](./consumer/src/shopping/CONTEXT.md) — end-customer browsing, basket, and ordering
  - [Security](./consumer/src/security/CONTEXT.md) — authentication for end customers

`business-common` and `system-common` are shared layers, not bounded contexts — they must not depend on any context (see `eslint.project-rules.ts` / `createLayerDependencyRules`).

## Relationships

- **admin: Catalog Management → Assets Management**: Catalog Management views resolve item images via Assets Management's asset helper.
- **consumer: Shopping (ordering) → Shopping (basket)**: within the Shopping context, the ordering domain reads the basket domain's state (same context, cross-domain — not a cross-context dependency).

## System-wide Language

These terms apply across every context in both apps, decided during the `src/pages` relocation planning (2026-09-17):

**Page**:
A Vue component directly registered as the target of a route (a `component:` in some context's `router/*.ts`). Pages live under a workspace's top-level `src/pages/`, organized by URL resource rather than by bounded context or domain.
_Avoid_: View (superseded — see below).

**View**:
_Deprecated as of the `src/pages` migration._ Previously meant the same thing as **Page** (a routed screen component living in a context's `views/` folder). New code should say **Page**; existing `*View.vue` files are renamed to `*Page.vue` as each context migrates.

**Public API** (of a context):
The single file at a bounded context's root (`public-api.ts`) that code outside the context — especially `src/pages` — must import through. A context's internal folders (`components/`, `services/`, `stores/`, `validation/`, etc.) must not be imported directly from outside the context.
_Avoid_: Barrel (too generic — this is specifically the cross-context boundary, not just any re-export file).
