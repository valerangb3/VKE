package com.vgb3.vke.data.mock

import com.vgb3.vke.data.dto.AppDetailsDto
import com.vgb3.vke.data.dto.AppDto
import com.vgb3.vke.data.dto.SimilarAppDto


object MockAppData {

    private const val TBANK_ICON_URL =
        "https://static.rustore.ru/imgproxy/jinnNvWDiuYKlR6rYbP9DedPzELHgbe8opPzXh8k8FE/preset:web_app_icon_62/plain/https://static.rustore.ru/apk/220863/content/ICON/2238e3ca-e3e7-41d0-b037-777ddc637a5b.png@webp"

    private const val GUILD_ICON_URL =
        "https://static.rustore.ru/imgproxy/APsbtHxkVa4MZ0DXjnIkSwFQ_KVIcqHK9o3gHY6pvOQ/preset:web_app_icon_62/plain/https://static.rustore.ru/apk/393868735/content/ICON/3f605e3e-f5b3-434c-af4d-77bc5f38820e.png@webp"

    val apps: List<AppDto> = listOf(
        AppDto(
            id = 1,
            appName = "Т-Банк",
            shortDescription = "Управляйте финансами в приложении...",
            iconUrl = TBANK_ICON_URL,
        ),
        AppDto(
            id = 2,
            appName = "Гильдия Героев",
            shortDescription = "Экшен ММО РПГ: собирайте отряд и сражайтесь с боссами",
            iconUrl = GUILD_ICON_URL,
        ),

        AppDto(
            id = 3,
            appName = "Т-Банк",
            shortDescription = "Управляйте финансами в приложении...",
            iconUrl = null,
        ),
    )


    fun appDetails(id: Long): AppDetailsDto = tbankDetails.copy(id = id)

    val similarApps: List<SimilarAppDto> = List(15) { index ->
        SimilarAppDto(
            packageName = "similar-app-$index",
            appName = "Т-Банк",
            iconUrl = TBANK_ICON_URL,
        )
    }

    private val tbankDetails = AppDetailsDto(
        id = 1,
        appName = "Т-Банк",
        companyName = "T-Bank",
        categoryName = "Финансы",
        ageLimit = 0,
        // 232.3 МБ в байтах: сервер присылает размер в байтах, в МБ переводит маппер
        fileSizeBytes = 243_584_205,
        iconUrl = TBANK_ICON_URL,
        screenshotUrls = listOf(
            "https://static.rustore.ru/imgproxy/pGCkIfaj1QDM9M07Zc-1JzrFtmEJNoKOamxIcLQBJv0/preset:web_scr_prt_162/plain/https://static.rustore.ru/2025/9/30/f1/apk/220863/content/SCREENSHOT/a99db8d2-1771-49b9-9dec-b965184a42a6.png@webp",
            "https://static.rustore.ru/imgproxy/1tIurY6QRC4MP-hlf__AGivKE07Gq0bkJcRFtdYZM1o/preset:web_scr_prt_162/plain/https://static.rustore.ru/2025/9/30/af/apk/220863/content/SCREENSHOT/2811bcf2-5e0e-4cd6-a7d5-a27cdd351f33.png@webp",
            "https://static.rustore.ru/imgproxy/waeaJ8NC9LR8cyW81FCE684Pz_qnJH4QeEK9Gb7cdS8/preset:web_scr_prt_162/plain/https://static.rustore.ru/2025/9/30/2a/apk/220863/content/SCREENSHOT/52a7432c-ca4d-4952-95cb-5b793764c894.png@webp",
            "https://static.rustore.ru/imgproxy/WDv1a3r7gvKmqAwL8KIZLepTgMOSM4jL2VXhNXSuyzs/preset:web_scr_prt_162/plain/https://static.rustore.ru/2025/9/30/f8/apk/220863/content/SCREENSHOT/59a2589b-29f0-4bcd-b8f8-e36b6f393c47.png@webp",
            "https://static.rustore.ru/imgproxy/GGXVxcP3I1qMpt_0I2_8ReBIUlU_bZ6WJjjIqJL5akc/preset:web_scr_prt_162/plain/https://static.rustore.ru/2025/9/30/24/apk/220863/content/SCREENSHOT/8228964f-e741-4445-aca9-47cca4bb81c0.png@webp",
            "https://static.rustore.ru/imgproxy/b9OBTcMbxpGNooRZakdTCDCSowhrFD8wALfB5z9maow/preset:web_scr_prt_162/plain/https://static.rustore.ru/2025/9/30/86/apk/220863/content/SCREENSHOT/b4d35726-6ca7-4e88-bbe6-932dbecfcede.png@webp",
            "https://static.rustore.ru/imgproxy/3NLxJI3cduYmfj6NcbWt41Pjv1fr2FSUUxQ2q8lgwVg/preset:web_scr_prt_162/plain/https://static.rustore.ru/2025/9/30/db/apk/220863/content/SCREENSHOT/ca45d94d-36f9-4342-a136-50a3b7f886e5.png@webp",
            "https://static.rustore.ru/imgproxy/JwXUBOiqSoBbQecgTbTDLFtKcuuFZw7traSAsfj7G-U/preset:web_scr_prt_162/plain/https://static.rustore.ru/2025/9/30/06/apk/220863/content/SCREENSHOT/e8029331-554b-4386-8e86-b8af98f3783d.png@webp"
        ),
        fullDescription = "Т-Банк — приложение для управления деньгами и картами онлайн. Оплачивайте ЖКХ, штрафы ГИБДД и госуслуги без комиссии, переводите через СБП, копите на вкладах и инвестируйте — всё под рукой.\n" +
            "\n" +
            "✅ Карты, вклады и инвестиции\n" +
            "\n" +
            "Дебетовая карта Black — пластиковая МИР или виртуальная. Оплачивайте счета бесконтактно и телефоном, получайте кэшбэк рублями за покупки. Если нужны дополнительные деньги, подойдет кредитка Платинум с лимитом до 1 000 000 ₽.\n" +
            "\n" +
            "Откройте вклад или накопительный счет, чтобы деньги работали. Если хочется большего, инвестируйте в акции российских компаний через брокерский счет прямо в приложении. Если не хочется выбирать ценные бумаги самому, есть готовые стратегии от аналитиков. Начать можно с 10 ₽.\n" +
            "\n" +
            "✅ Страхование\n" +
            "\n" +
            "Оформите ОСАГО, каско или страховку имущества — без очередей и лишних документов.\n" +
            "\n" +
            "✅ Кэшбэк и выгода\n" +
            "\n" +
            "Каждый месяц выбирайте 4 категории повышенного кэшбэка до 15%. В приложении более 100 предложений от известных брендов с кэшбэком до 30%, а в рубрике «кэшбэк дня» — лучшее предложение на сегодня.\n" +
            "\n" +
            "Покупайте билеты, заказывайте еду, заправляйте машину — всё с кэшбэком до 30%. Платите долями или в рассрочку — без переплат. Подписка Pro и сервис Premium увеличат выгоду от каждой траты.\n" +
            "\n" +
            "✅ Управление деньгами и бюджетом\n" +
            "\n" +
            "Оплачивайте налоги, ЖКХ, квартплату и штрафы без комиссии. Переводить деньги и оплачивать счета онлайн удобнее с умной камерой: распознает номера карт, квитанции, QR-коды и написанные от руки номера телефонов.\n" +
            "\n" +
            "В разделах Дом и Авто собраны траты по этим тематикам, плюс оценка рыночной стоимости машины и квартиры. Меняйте категории операций, чтобы точнее вести бюджет и следить за расходами, или доверьте это сервису Финздоровье. Он считает общий баланс электронного кошелька, показывает кредитный рейтинг и дает рекомендации, что делать с деньгами.\n" +
            "\n" +
            "Пополняйте копилку и откладывайте на цели: функция «Письмо себе» напомнит, стоит ли тратить деньги с накопительного счета. Если хотите скрыть балансы в публичном месте — переверните телефон.\n" +
            "\n" +
            "AI-помощники дадут рекомендации, выполнят поручения и подстроятся под ваш стиль общения.\n" +
            "\n" +
            "✅ Защита денег и данных\n" +
            "\n" +
            "«Защитим или вернем деньги» определяет мошенничество во время звонка с вероятностью выше 99%. А если мошенники всё же получат деньги, мы их вернем. Работает вместе с сим-картой Т-Мобайла,\n" +
            "\n" +
            "«Защита близких» помогает уберечь деньги семьи от мошенников: переводы и кредиты родных проходят с вашего согласия.\n" +
            "\n" +
            "T-ID — быстрый и безопасный вход на сайты и в приложения: сохраняет пароли и автоматически заполняет анкеты.\n" +
            "\n" +
            "Тарифы и условия по продуктам банка: ",
    )
}
