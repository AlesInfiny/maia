# Regex Usage Ledger (Semgrep)

- Generated: 2026-01-28 14:50:16
- Findings: 3

## Summary

| API kind | Count |
|---|---:|
| String.replaceAll | 2 |
| Spring MVC Mapping (may include {var:regex}) | 1 |

## Details

### `system-common/src/main/java/com/dressca/cms/systemcommon/config/I18nConfig.java`

- **String.replaceAll**  
  - Location: `system-common/src/main/java/com/dressca/cms/systemcommon/config/I18nConfig.java:40-41`  
  - Severity: `INFO`  
  - Engine: `unknown`  
  - Rule: `semgrep.java.regex.string.replaceall`
  - Message: String.replaceAll() による正規表現の使用

```java
            String uriStr = resource.getURI().toString();
            // "classpath:"付きのベース名を抽出
            return "classpath:" + uriStr
                .replaceAll("^.*?/i18n/", "i18n/")
                .replaceAll("(_[a-z]{2}(_[A-Z]{2})?)?\\.properties$", "");
          } catch (IOException e) {
```

- **String.replaceAll**  
  - Location: `system-common/src/main/java/com/dressca/cms/systemcommon/config/I18nConfig.java:40-42`  
  - Severity: `INFO`  
  - Engine: `unknown`  
  - Rule: `semgrep.java.regex.string.replaceall`
  - Message: String.replaceAll() による正規表現の使用

```java
            String uriStr = resource.getURI().toString();
            // "classpath:"付きのベース名を抽出
            return "classpath:" + uriStr
                .replaceAll("^.*?/i18n/", "i18n/")
                .replaceAll("(_[a-z]{2}(_[A-Z]{2})?)?\\.properties$", "");
          } catch (IOException e) {
            throw new RuntimeException(e);
```

### `web/src/main/java/com/dressca/cms/web/controller/AnnouncementController.java`

- **Spring MVC Mapping (may include {var:regex})**  
  - Location: `web/src/main/java/com/dressca/cms/web/controller/AnnouncementController.java:57-57`  
  - Severity: `INFO`  
  - Engine: `unknown`  
  - Rule: `semgrep.java.spring.requestmapping.regex`
  - Message: @RequestMapping / @GetMapping 等の regex 使用

```java
 */
@Controller
@RequestMapping("/announcements")
@RequiredArgsConstructor
public class AnnouncementController {
```
