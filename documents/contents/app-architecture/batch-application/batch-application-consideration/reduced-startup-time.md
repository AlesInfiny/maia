---
title: バッチ アプリケーション編
description: バッチアプリケーションでの検討事項として 起動に時間がかかる点について解説します。
---

# バッチアプリケーションの起動に時間がかかる {#top}

大量で反復的なデータを処理するバッチ処理を行う場合、定められた実行時間で処理を完了できることは重要な要素です。
しかし、本アーキテクチャで利用している Spring Boot アプリケーションにおいては、 Bean の初期化を起動時に実行する特性からアプリケーションの起動に時間がかかります。
そのため、バッチ処理を実行するたびに Spring Boot アプリケーションを起動することは、定められた実行時間での処理を妨げる可能性があります。
このような場合、以下の方法による解決が挙げられます。

## バッチアプリケーションを常駐プロセス化する {#resident-process}

バッチアプリケーションを常駐プロセス化することは起動時間短縮の 1 つの方法です。
常駐プロセスとは、 Web アプリケーションのような形でサーバー開始時からあらかじめアプリケーションを起動しておくプロセスを指します。

バッチアプリケーションを常駐プロセス化することで、 Bean の初期化等にかかる時間が常駐プロセス起動時のみとなるため、バッチ処理時にはこの起動時間を意識する必要がありません。

常駐プロセス化する際の各バッチ処理方式の実装方法は以下のようになります。

### 適時バッチ {#timely-batch}

適時バッチを実装する際には、常駐プロセス化した REST API アプリケーションを構築しジョブを開始する方法があります。
REST API アプリケーションの構築方法は以下の通りです。

- Spring Boot アプリケーションを利用したバッチ処理

    サードパーティーのジョブ管理ツールをすでに利用しており、ジョブは実行するだけにしたい場合は、 Spring Batch のライブラリを利用せず Spring Boot アプリケーションを用いてバッチ処理をすることは有効です。
    また、ジョブ実行が小規模で単純な処理で構成されており Spring Batch の機能を必要としない場合も、 Spring Boot アプリケーションを利用するとよいでしょう。

- [イベント駆動型の Spring Batch アプリケーション :material-open-in-new:](https://spring.pleiades.io/spring-batch/reference/spring-batch-integration/launching-jobs-through-messages.html){ target=_blank } を利用したバッチ処理

    Spring Batch の中には、アプリケーションを常駐プロセス化し API を使用してバッチジョブを開始する機能が提供されています。
    ジョブのスケジューリングやジョブの履歴閲覧などのジョブ管理を Spring Batch 上で行いたい場合や、大規模で複雑なバッチ処理を限られた実行時間でこなす必要がある場合には、本機能を利用して常駐プロセス化する方法が有効となるでしょう。

### 常駐バッチ {#resident-batch}

常駐バッチを実装する際には、上記で示した適時バッチの方法で常駐プロセス化するほかに、定期的にジョブを実行するためのスケジューリング機能を利用する必要があります。
スケジューリング機能の実装方法は以下が挙げられます。

- ジョブ管理ツールのスケジューリング機能を利用する

    サードパーティーのジョブ管理ツールには、スケジューリング機能も提供されています。
    ジョブを実行する Spring Boot アプリケーションを定期的に実行するようスケジューリングすることで常駐バッチを実装できます。

- Spring Boot のスケジューリング機能を利用する

    ジョブ管理を Spring Batch の機能で実行する場合、 Spring Boot で提供されているスケジューリング機能を利用します。
    [`#!java @Scheduled` :material-open-in-new:](https://spring.pleiades.io/guides/gs/scheduling-tasks){ target=_blank } は、定期的にバッチ処理を実行できる機能です。
    このアノテーションを利用すれば、一度アプリケーションを起動した後は定期的にバッチ処理が自動実行されるようになり、 Bean の初期化にかかる起動時間を短縮できます。

## クラウドサービスの FaaS でバッチ処理を実行する {#batch-processing-by-cloud}

バッチ処理を実行するアプリケーションをクラウドサービスの FaaS に配置することで、起動時間の短縮が期待できます。
FaaS に配置することによる起動時間短縮の実現方法として、以下が挙げられます。

### ホットスタンバイによるアプリケーションの常時起動 {#batch-processing-by-hot-stand-by}

一部 FaaS のサービスでは、実行時に起動したアプリケーションをホットスタンバイ状態にし、しばらくリクエストを待機する機能があります。
定期的に FaaS のサービスを呼び出し常時ホットスタンバイ状態にすることで、アプリケーションが起動している状態でバッチ処理を実行できます。
この方法を実現するためには、利用する FaaS サービスがホットスタンバイ機能に対応しているかを確認してください。

### インタプリタ言語に置き換えることによる起動高速化 {#batch-processing-using-interpreter}

クラウドサービスの FaaS は、多様なプログラミング言語に対応しています。
本アーキテクチャで利用している Java のようなプログラミング言語は、始めに中間言語としてコンパイルし、実行時に JIT コンパイルで中間言語を機械語に変換して処理します。
このような方式は、 OS や CPU に依存しない中間言語のモジュールを様々な環境で実行できる利点がありますが、 中間言語を JIT コンパイルして機械語に変換するまでの初期化処理のため、起動に時間がかかります。
これに対し、 JavaScript や Python といったインタプリタ言語は、 1 行ずつ機械語へ変換しながら実行するため、初期化時の変換処理がない分起動時間を短縮できます。
ただし、インタプリタ言語は Java のようなプログラミング言語と比較して、機械語へ逐次変換することから実行速度が遅くなる場合もあり、大規模な処理が必要な場合は起動時間が短縮されてもトータルの実行時間が短縮されません。
バッチ処理が小規模で複雑でない場合には、インタプリタ言語に書き換えるとよいでしょう。

また、バッチアプリケーションを FaaS サービスに配置することで、起動時間短縮の他にも以下の利点があげられます。

### 分散・並列処理による実行時間の高速化 {#batch-processing-by-distributed-and-parallel-processing}

クラウドサービスは、大規模なコンピューティングリソースを提供しています。これらのリソースは必要な時に必要な分だけ利用でき、柔軟かつ効率的なコンピューティングが可能となります。
クラウドサービスを利用してバッチ処理を行うジョブを複数のコンピューティングリソースに分散配置することで、各ジョブが独立したリソース上で同時に実行され、並列実行が可能となります。
また、クラウドサービスのオートスケーリング機能を利用することで、バッチ処理の負荷に応じた自動的なスケールアウト・スケールインが実現できます。
これにより、定められた実行時間内で処理するために必要なリソースを自動的に最適化できます。
このように、バッチ処理に FaaS を利用することでシステムのパフォーマンスが一定に保たれた状態で大量のジョブを効率的に処理でき、実行時間を高速化できます。

### GraalVM を利用した実行 {#graal-vm}

[GraalVM :material-open-in-new:](https://www.oracle.com/jp/java/graalvm/what-is-graalvm/){ target=_blank } は、 Java アプリケーションをネイティブなバイナリー実行ファイル（ネイティブイメージ）に AOT コンパイルする機能を持った JDK です。
GraalVM を利用することで、 JVM を介さず直接実行できるネイティブイメージを作成することが可能となり、実行速度の高速化やメモリ使用量の削減を期待できます。

GraalVM を利用したサンプルアプリケーションの実行検証については、以下をご確認ください。

- [GraalVM の実行検証記事 :material-open-in-new:](https://qiita.com/RyoNakagawa2/items/c0b29955cb7f1bfd7c75){ target=_blank }
