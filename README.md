LemPORT
=======

A Lemmatizer for Portuguese
---------------------------

[Rodrigues, R., Gonçalo Oliveira, H., Gomes, P.: LemPORT: a High-Accuracy Cross-Platform Lemmatizer for Portuguese. In: Pereira, M.J.V., Leal, J.P., Simões, A. (eds.) Proceedings of the 3rd Symposium on Languages, Applications and Technologies (SLATE'14). pp. 267-274. OpenAccess Series in Informatics, Schloss Dagstuhl - Leibniz-Zentrum für Informatik, Dagstuhl Publishing, Germany (June 2014)](http://drops.dagstuhl.de/opus/volltexte/2014/4575/pdf/23.pdf)

USAGE: For using this tool, add any JAR files found in the "lib" folder to your project's classpath. Those files include a JAR of the tool and its dependencies (if any).


Elixir OTP Integration
---------------------------

This is repository is based on [rikarudo/LemPORT](https://github.com/rikarudo/LemPORT) and adds Erlang's OTP communication to use this lemmatizer with an Erlang/Elixir otp application. This is done with the usage of [Erlang's JInterface](http://erlang.org/doc/man/jinterface.html).

USAGE: "gradle build" inside of the repository will build a jar file in build/libs/LemPORT-X.X.jar
