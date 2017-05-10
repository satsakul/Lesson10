# Lesson10
Lesson10
1. Ходим в сеть на UI потоке.
На версиях адроида от HoneyComb (android 3.0) система не позволит сделать запрос на UI thread.
На версиях android ниже api 11(HoneyComb) - будут заметны лаги, а также получены ANR.
https://developer.android.com/training/articles/perf-anr.html
http://microsin.net/programming/android/keeping-your-app-responsive.html
git checkout yandex_fotki1

2. Работаем с настройкой android-эмулятора для тестирования работы приложения в разных условиях сети

3. Разбираем ANR:
adb shell "cat data/anr/traces.txt"

4. Deadlock - ?
git checkout deadlock

5. StrictMode
https://developer.android.com/reference/android/os/StrictMode.html
https://habrahabr.ru/post/152571/

5.1. StrictMode с настройкой всех политик регулирования опрераций и анализ через лог
git checkout strict_mode1

5.2. StrictMode с настройкой всех политик регулирования опрераций и анализ через диалоговое окно
git checkout strict_mode2

5.3. StrictMode с настройкой определённых политик регулирования опреаций с выводом в dropbox
adb shell dumpsys dropbox data_app_strictmode --print
git checkout strict_mode3

6. Переводим загрузку изображения на другой поток. Ловим CalledFromWrongThreadException
git checkout worker_thread

7. Handler
https://developer.android.com/reference/android/os/Handler.html
https://habrahabr.ru/post/142256/
http://javaway.info/mnogopotochnost-v-android-looper-handler-handlerthread-chast-1/
http://www.vogella.com/tutorials/AndroidBackgroundProcessing/article.html#purpose-of-the-asynctask-class
http://startandroid.ru/ru/uroki/vse-uroki-spiskom/143-urok-80-handler-nemnogo-teorii-nagljadnyj-primer-ispolzovanija.html

7.1. Создаёи handler в главном потоке и вызываем его из worker thread
http://startandroid.ru/ru/uroki/vse-uroki-spiskom/147-urok-84-handler-obrabotka-runnable.html
git checkout handler1

7.2. Создаёи handler в worker thread c main looper в кач-ве параметра в конструкторе
git checkout handler2

7.3. Работаем с одним экземпляром Thread. Получаем ошибку при попытке использовать его повторно.
git checkout handler3

7.4. Работаем с экземпляром HandlerThread.
git checkout handler4

7.5.  Демонстрация очереди/отложенной очереди Handler. - ?
git checkout handler5

8. AsyncTask
https://developer.android.com/reference/android/os/AsyncTask.html
http://www.vogella.com/tutorials/AndroidBackgroundProcessing/article.html#purpose-of-the-asynctask-class
http://startandroid.ru/ru/uroki/vse-uroki-spiskom/149-urok-86-asynctask-znakomstvo-neslozhnyj-primer.html

8.1. Загрузка картинок через AsyncTask. Создание одного экземпляра AsyncTask.
Получаем ошибку при повторном использовании.
git checkout asynctask1

8.2. Загрузка картинок через AsyncTask. Создание нового экземпляра AsyncTask для каждой загрузки.
git checkout asynctask2

8.3. Отмена загрузки картинок через AsyncTask.
git checkout asynctask3

8.4. AsyncTask. Поворот экрана.
Выводим в лог идентификаторы Activity/AsyncTask.
Убеждаемся, что при повороте создаётся новые Activity/AsyncTask, но предыдущий продолжает работать.
git checkout asynctask4

8.5. AsyncTask. Поворот экрана. Leak Memory.
https://developer.android.com/studio/profile/am-memory.html

8.6. AsyncTask. Поворот экрана. LeakCanary
https://github.com/square/leakcanary
git checkout asynctask5_leakcanary

8.7. AsyncTask. Запускаем AsyncTask в сторонней библиотеке и у себя.
С версии android 3.0 выполнение AsyncTask будет поставлено в очередь.
Смотрим, как на 10 api работа выполняется параллельно, а на версии выше android 3.0 последовательно.
git checkout asynctask6_library

8.8. AsyncTask. Запускаем AsyncTask используя THREAD_POOL_EXECUTOR
git checkout asynctask7_library

8.9. AsyncTask. Избавляемся от ликов. Temp
git checkout asynctask8

9. Service
https://developer.android.com/guide/components/services.html?hl=ru
http://developer.alexanderklimov.ru/android/theory/services-theory.php

9.1. Service. Запуск задачи в сервисе. Запускаем сервис и ждём броадкасты. Когда броадкаст приходит, обновляем UI
https://developer.android.com/guide/components/services.html?hl=ru
git checkout service_load_image1

9.2. IntentService. Запуск задачи в сервисе.
git checkout service_load_image2

9.3. Service. Запуск задачи в сервисе. Привязывание к службе. Получение запроса синхронно
https://developer.android.com/guide/components/bound-services.html?hl=ru
git checkout service_load_image3
