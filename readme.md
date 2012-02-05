##Java-проекты для API Яндекс.Денег

- - -

**aggregator** (aka Java Yandex Money Api) - проект-агрегатор.

**yamolib** (aka Yandex Money Library) - java-библиотека для работы с API. [Посмотреть](https://github.com/melnikovdv/Java-Yandex.Money-API-SDK/tree/master/yamolib).

**yamodroid** (aka Yandex Money Droid Library) - android-библиотека для работы c API. [Посмотреть](https://github.com/melnikovdv/Java-Yandex.Money-API-SDK/tree/master/yamodroid).

**samples** - проект-агрегатор для тестовых приложений использования библиотек. [Посмотреть](https://github.com/melnikovdv/Java-Yandex.Money-API-SDK/tree/master/samples).

**yamolib-sample** - тестовое приложение для yamolib. [Посмотреть](https://github.com/melnikovdv/Java-Yandex.Money-API-SDK/tree/master/samples/yamolib-sample).

**yamodroid-sample** - тестовое приложение для yamodroid. [Посмотреть](https://github.com/melnikovdv/Java-Yandex.Money-API-SDK/tree/master/samples/yamodroid-sample).

###Структура проекта

    aggregator
    |
    |- yamolib
    |
    |- yamodroid
    |
    |- samples
        |
        |- yamolib-sample
        |
        |- yamodroid-sample

###Инструкции по сборке

Все проекты можно собрать с помощью [Maven](http://en.wikipedia.org/wiki/Apache_Maven) и pom-файлов. Инструкция по установке Maven [тут](http://maven.apache.org/download.html).   
GettingStarted Maven-плагина для Android [тут](http://code.google.com/p/maven-android-plugin/wiki/GettingStarted).

Чтобы создать файлы проекта для вашей среды разработки, нужно в каталоге какого-либо проекта выполнить команду `mvn idea:idea` или `mvn eclipse:eclipse` в зависимости от вашей IDE. NetBeans не упомянут в виду отсутствия у него плагина для android-разработки.