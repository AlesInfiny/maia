# Regex Usage Ledger (Semgrep)

- Generated: 2026-01-28 11:54:13
- Findings: 11

## Summary

| API kind | Count |
|---|---:|
| Spring MVC Mapping (may include {var:regex}) | 11 |

## Details

### `web-admin/src/main/java/com/dressca/web/admin/controller/AssetsController.java`

- **Spring MVC Mapping (may include {var:regex})**  
  - Location: `web-admin/src/main/java/com/dressca/web/admin/controller/AssetsController.java:34-34`  
  - Severity: `INFO`  
  - Engine: `unknown`  
  - Rule: `semgrep.java.spring.requestmapping.regex`
  - Message: @RequestMapping / @GetMapping 等の regex 使用

```java
@RestController
@Tag(name = "Assets", description = "アセットの情報にアクセスする API です。")
@RequestMapping("/api/assets")
@AllArgsConstructor
@PreAuthorize(value = "isAuthenticated()")
```

### `web-admin/src/main/java/com/dressca/web/admin/controller/CatalogBrandsController.java`

- **Spring MVC Mapping (may include {var:regex})**  
  - Location: `web-admin/src/main/java/com/dressca/web/admin/controller/CatalogBrandsController.java:29-29`  
  - Severity: `INFO`  
  - Engine: `unknown`  
  - Rule: `semgrep.java.spring.requestmapping.regex`
  - Message: @RequestMapping / @GetMapping 等の regex 使用

```java
@RestController
@Tag(name = "CatalogBrands", description = "カタログブランドの情報にアクセスする API です。")
@RequestMapping("/api/catalog-brands")
@AllArgsConstructor
@PreAuthorize(value = "isAuthenticated()")
```

### `web-admin/src/main/java/com/dressca/web/admin/controller/CatalogCategoriesController.java`

- **Spring MVC Mapping (may include {var:regex})**  
  - Location: `web-admin/src/main/java/com/dressca/web/admin/controller/CatalogCategoriesController.java:29-29`  
  - Severity: `INFO`  
  - Engine: `unknown`  
  - Rule: `semgrep.java.spring.requestmapping.regex`
  - Message: @RequestMapping / @GetMapping 等の regex 使用

```java
@RestController
@Tag(name = "CatalogCategories", description = "カタログカテゴリの情報にアクセスする API です。")
@RequestMapping("/api/catalog-categories")
@AllArgsConstructor
@PreAuthorize(value = "isAuthenticated()")
```

### `web-admin/src/main/java/com/dressca/web/admin/controller/CatalogItemsController.java`

- **Spring MVC Mapping (may include {var:regex})**  
  - Location: `web-admin/src/main/java/com/dressca/web/admin/controller/CatalogItemsController.java:50-50`  
  - Severity: `INFO`  
  - Engine: `unknown`  
  - Rule: `semgrep.java.spring.requestmapping.regex`
  - Message: @RequestMapping / @GetMapping 等の regex 使用

```java
@RestController
@Tag(name = "CatalogItems", description = "カタログアイテムの情報にアクセスする API です。")
@RequestMapping("/api/catalog-items")
@AllArgsConstructor
@PreAuthorize(value = "hasAuthority('" + UserRoleConstants.ADMIN + "')")
```

### `web-admin/src/main/java/com/dressca/web/admin/controller/UsersController.java`

- **Spring MVC Mapping (may include {var:regex})**  
  - Location: `web-admin/src/main/java/com/dressca/web/admin/controller/UsersController.java:23-23`  
  - Severity: `INFO`  
  - Engine: `unknown`  
  - Rule: `semgrep.java.spring.requestmapping.regex`
  - Message: @RequestMapping / @GetMapping 等の regex 使用

```java
@RestController
@Tag(name = "Users", description = "ログイン中のユーザーの情報にアクセスする API です。")
@RequestMapping("/api/users")
@PreAuthorize(value = "isAuthenticated()")
public class UsersController {
```

### `web-consumer/src/main/java/com/dressca/web/consumer/controller/AssetsController.java`

- **Spring MVC Mapping (may include {var:regex})**  
  - Location: `web-consumer/src/main/java/com/dressca/web/consumer/controller/AssetsController.java:41-41`  
  - Severity: `INFO`  
  - Engine: `unknown`  
  - Rule: `semgrep.java.spring.requestmapping.regex`
  - Message: @RequestMapping / @GetMapping 等の regex 使用

```java
@RestController
@Tag(name = "Assets", description = "アセットの情報にアクセスする API です。")
@RequestMapping("/api/assets")
@AllArgsConstructor
public class AssetsController {
```

### `web-consumer/src/main/java/com/dressca/web/consumer/controller/BasketItemController.java`

- **Spring MVC Mapping (may include {var:regex})**  
  - Location: `web-consumer/src/main/java/com/dressca/web/consumer/controller/BasketItemController.java:52-52`  
  - Severity: `INFO`  
  - Engine: `unknown`  
  - Rule: `semgrep.java.spring.requestmapping.regex`
  - Message: @RequestMapping / @GetMapping 等の regex 使用

```java
@RestController
@Tag(name = "BasketItems", description = "買い物かごアイテムの情報にアクセスする API です。")
@RequestMapping("/api/basket-items")
@AllArgsConstructor
public class BasketItemController {
```

### `web-consumer/src/main/java/com/dressca/web/consumer/controller/CatalogBrandsController.java`

- **Spring MVC Mapping (may include {var:regex})**  
  - Location: `web-consumer/src/main/java/com/dressca/web/consumer/controller/CatalogBrandsController.java:28-28`  
  - Severity: `INFO`  
  - Engine: `unknown`  
  - Rule: `semgrep.java.spring.requestmapping.regex`
  - Message: @RequestMapping / @GetMapping 等の regex 使用

```java
@RestController
@Tag(name = "CatalogBrands", description = "カタログブランドの情報にアクセスする API です。")
@RequestMapping("/api/catalog-brands")
@AllArgsConstructor
public class CatalogBrandsController {
```

### `web-consumer/src/main/java/com/dressca/web/consumer/controller/CatalogCategoriesController.java`

- **Spring MVC Mapping (may include {var:regex})**  
  - Location: `web-consumer/src/main/java/com/dressca/web/consumer/controller/CatalogCategoriesController.java:28-28`  
  - Severity: `INFO`  
  - Engine: `unknown`  
  - Rule: `semgrep.java.spring.requestmapping.regex`
  - Message: @RequestMapping / @GetMapping 等の regex 使用

```java
@RestController
@Tag(name = "CatalogCategories", description = "カタログカテゴリの情報にアクセスする API です。")
@RequestMapping("/api/catalog-categories")
@AllArgsConstructor
public class CatalogCategoriesController {
```

### `web-consumer/src/main/java/com/dressca/web/consumer/controller/CatalogItemsController.java`

- **Spring MVC Mapping (may include {var:regex})**  
  - Location: `web-consumer/src/main/java/com/dressca/web/consumer/controller/CatalogItemsController.java:30-30`  
  - Severity: `INFO`  
  - Engine: `unknown`  
  - Rule: `semgrep.java.spring.requestmapping.regex`
  - Message: @RequestMapping / @GetMapping 等の regex 使用

```java
@RestController
@Tag(name = "CatalogItems", description = "カタログアイテムの情報にアクセスする API です。")
@RequestMapping("/api/catalog-items")
@AllArgsConstructor
public class CatalogItemsController {
```

### `web-consumer/src/main/java/com/dressca/web/consumer/controller/OrderController.java`

- **Spring MVC Mapping (may include {var:regex})**  
  - Location: `web-consumer/src/main/java/com/dressca/web/consumer/controller/OrderController.java:48-48`  
  - Severity: `INFO`  
  - Engine: `unknown`  
  - Rule: `semgrep.java.spring.requestmapping.regex`
  - Message: @RequestMapping / @GetMapping 等の regex 使用

```java
@Tag(name = "Orders", description = "注文の情報にアクセスする API です。")
@AllArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {
```
