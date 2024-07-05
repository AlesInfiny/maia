---
title: バッチアプリケーション編
description: バッチアプリケーションでの検討事項としてメタデータテーブルを作成しない方法について解説します。
---

# Spring Batch のメタデータテーブルを作成したくない {#batch-job-management-by-third-party-tool}

本サンプルアプリケーションは、 Spring Batch のアーキテクチャを利用してバッチ処理を実現しています。
Spring Batch では、バッチの実行履歴や実行状態の保存といったジョブ管理のために、メタデータテーブルを作成しています。
作成されるメタデータテーブルについては、[メタデータテーブルを作成するスキーマ :material-open-in-new:](https://spring.pleiades.io/spring-batch/reference/schema-appendix.html){ target=_blank } をご覧ください。

クラウドサービスのリソースにバッチアプリケーションを配置する場合や、バッチアプリケーションのジョブ管理を特定のジョブ管理ツールに任せる場合、 Spring Batch でジョブを管理するためのメタデータテーブルは必要ありません。

しかしながら、 Spring Batch の仕様上、メタデータテーブルがなければバッチ処理は実行されず、[エラー :material-open-in-new:](https://github.com/spring-projects/spring-batch/issues/4485){ target=_blank } が発生します。
そのため、 Spring Batch を利用するうえで作成が必須とされるメタデータテーブルに対し、とりうる対応について説明します。

## メタデータテーブルをインメモリデータベースに作成するよう設定する {#definition-meta-data-table-by-in-memory-db}

Spring Batch では、メインのデータベースとは別にメタデータテーブル用のセカンダリデータベースを設定できます。
メタデータテーブル用のデータベースに `h2` のようなインメモリデータベースを指定することで、アプリケーションの終了と共にメタデータテーブルが削除されるようになります。

メタデータテーブル用のセカンダリデータベースを設定するためには、 [@BatchDataSource アノテーション :material-open-in-new:](https://spring.pleiades.io/spring-boot/api/java/org/springframework/boot/autoconfigure/batch/BatchDataSource.html){ target=_blank } を利用できます。

!!! warning "バッチアプリケーションを常時稼働させる際の注意点"

    [@Scheduled アノテーション :material-open-in-new:](https://spring.pleiades.io/guides/gs/scheduling-tasks){ target=_blank } を利用するなどでアプリケーションを常時稼働させ続ける場合、バッチ処理の終了でアプリケーションが終了しません。
    これにより、メタデータテーブルの削除が行われずインメモリデータべース内にデータが蓄積され続けることでメモリが消費され、 Out Of Memory Error となる可能性があります。
    このような場合、以下に示すような対応が必要になります。

    - ジョブ管理ツール上で適時バッチを定期的に実行するようスケジューリングすることで、バッチ処理の完了と共にアプリケーションが必ず終了するようにする
    - バッチ処理の終了と共にメタデータテーブルの削除処理を行うよう変更する
    - メタデータがメインのデータベースに蓄積されることを許容し、ジョブ管理ツール上でデータ削除用のジョブを設定する（後述）

## ジョブ管理ツール上で定期的にメタデータを削除するよう設定する {#delete-meta-data-table-on-tools}

メインのデータベースにメタデータテーブルが作成されることを許容し、定期的にデータを削除するよう設定する方法です。
日次や週次といったタイミングで、自動的にメタデータテーブル内のデータが削除されるよう設定するとよいでしょう。
