# Regex Usage Ledger (Semgrep)

- Generated: 2026-01-28 14:43:18
- Findings: 2

## Summary

| API kind | Count |
|---|---:|
| Spring MVC Mapping (may include {var:regex}) | 2 |

## Details

### `web/src/main/java/com/dressca/web/controller/ServerTimeController.java`

- **Spring MVC Mapping (may include {var:regex})**  
  - Location: `web/src/main/java/com/dressca/web/controller/ServerTimeController.java:22-22`  
  - Severity: `INFO`  
  - Engine: `unknown`  
  - Rule: `semgrep.java.spring.requestmapping.regex`
  - Message: @RequestMapping / @GetMapping 等の regex 使用

```java
@RestController
@Tag(name = "ServerTime", description = "認証不要でサーバーの現在時刻を取得します。")
@RequestMapping("/api/servertime")
public class ServerTimeController {
```

### `web/src/main/java/com/dressca/web/controller/UserController.java`

- **Spring MVC Mapping (may include {var:regex})**  
  - Location: `web/src/main/java/com/dressca/web/controller/UserController.java:24-24`  
  - Severity: `INFO`  
  - Engine: `unknown`  
  - Rule: `semgrep.java.spring.requestmapping.regex`
  - Message: @RequestMapping / @GetMapping 等の regex 使用

```java
@RestController
@Tag(name = "Users", description = "認証済みユーザのユーザ ID を取得する API です。")
@RequestMapping("/api/users")
public class UserController {
```
