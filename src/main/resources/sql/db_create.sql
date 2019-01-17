-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema periodika
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `periodika` ;

-- -----------------------------------------------------
-- Schema periodika
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `periodika` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci ;
USE `periodika` ;

-- -----------------------------------------------------
-- Table `periodika`.`periodical_category`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `periodika`.`periodical_category` ;

CREATE TABLE IF NOT EXISTS `periodika`.`periodical_category` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `type` INT NOT NULL,
  `description` TEXT NULL,
  PRIMARY KEY (`id`));


-- -----------------------------------------------------
-- Table `periodika`.`user_type`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `periodika`.`user_type` ;

CREATE TABLE IF NOT EXISTS `periodika`.`user_type` (
  `id` INT NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `periodika`.`user`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `periodika`.`user` ;

CREATE TABLE IF NOT EXISTS `periodika`.`user` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `user_type_id` INT(11) NOT NULL,
  `first_name` VARCHAR(45) NOT NULL,
  `middle_name` VARCHAR(45) NULL DEFAULT NULL,
  `last_name` VARCHAR(45) NOT NULL,
  `email` VARCHAR(45) NULL DEFAULT NULL,
  `address` VARCHAR(255) NOT NULL,
  `phone` VARCHAR(45) NULL DEFAULT NULL,
  `login` VARCHAR(45) NOT NULL,
  `password` VARCHAR(32) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_user_user_type_idx` (`user_type_id` ASC) VISIBLE,
  UNIQUE INDEX `login_UNIQUE` (`login` ASC) VISIBLE,
  CONSTRAINT `fk_user_user_type`
    FOREIGN KEY (`user_type_id`)
    REFERENCES `periodika`.`user_type` (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 8
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `periodika`.`periodical`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `periodika`.`periodical` ;

CREATE TABLE IF NOT EXISTS `periodika`.`periodical` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(45) NOT NULL DEFAULT '',
  `description` TEXT NULL,
  `periodical_category_id` INT NOT NULL,
  `min_subscription_period` INT NOT NULL DEFAULT 1,
  `issues_per_period` INT NOT NULL DEFAULT 1,
  `price_per_period` DECIMAL(10,2) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  INDEX `fk_periodical_category1_idx` (`periodical_category_id` ASC) VISIBLE,
  CONSTRAINT `fk_periodical_category1`
    FOREIGN KEY (`periodical_category_id`)
    REFERENCES `periodika`.`periodical_category` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `periodika`.`payment`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `periodika`.`payment` ;

CREATE TABLE IF NOT EXISTS `periodika`.`payment` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `date` TIMESTAMP NOT NULL,
  `sum` DECIMAL(10,2) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `periodika`.`subscription`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `periodika`.`subscription` ;

CREATE TABLE IF NOT EXISTS `periodika`.`subscription` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT NOT NULL,
  `periodical_id` INT NOT NULL,
  `payment_id` INT NULL,
  `period_start` DATE NOT NULL,
  `period_count` INT NOT NULL,
  `period_end` DATE NOT NULL,
  `quantity` INT NOT NULL DEFAULT 1,
  `sum` DECIMAL(10,2) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_user_subscription_user1_idx` (`user_id` ASC) VISIBLE,
  INDEX `fk_user_subscription_periodical1_idx` (`periodical_id` ASC) VISIBLE,
  INDEX `fk_subscription_paiment1_idx` (`payment_id` ASC) VISIBLE,
  CONSTRAINT `fk_user_subscription_periodical1`
    FOREIGN KEY (`periodical_id`)
    REFERENCES `periodika`.`periodical` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_subscription_paiment1`
    FOREIGN KEY (`payment_id`)
    REFERENCES `periodika`.`payment` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_subscription_user1`
    FOREIGN KEY (`user_id`)
    REFERENCES `periodika`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

-- -----------------------------------------------------
-- Data for table `periodika`.`periodical_category`
-- -----------------------------------------------------
START TRANSACTION;
USE `periodika`;
INSERT INTO `periodika`.`periodical_category` (`id`, `name`, `type`, `description`) VALUES (1, 'Суспільство і держава', 1, 'Все, что нужно знать об обществе и государстве, о взаимодействии между ними, о влиянии одной стороны на другую. Мы редко поднимаем вопросы, касающиеся тем общества и государства, а уже давно эти темы популярны и востребованы социумом.');
INSERT INTO `periodika`.`periodical_category` (`id`, `name`, `type`, `description`) VALUES (2, 'Fashion & Style', 2, 'Стиль. Мода. Красота');
INSERT INTO `periodika`.`periodical_category` (`id`, `name`, `type`, `description`) VALUES (3, 'Фінанси та право', 1, 'Бухоблік. Оплата праці');
INSERT INTO `periodika`.`periodical_category` (`id`, `name`, `type`, `description`) VALUES (4, 'Видання для всіх', 1, 'Дім та присадибна ділянка. Календарі. Малюкам. Школярам. Молоді. Для домашнього кола');
INSERT INTO `periodika`.`periodical_category` (`id`, `name`, `type`, `description`) VALUES (5, 'Технологии і техніка', 2, 'Все о развитии и поддержке IT технологий, авиации, агротехнике, транспортной, бытовой технике и многое другое Вы можете почерпнуть из полезных изданий, которые находятся в разделе \"Технологии и техника\".');

COMMIT;


-- -----------------------------------------------------
-- Data for table `periodika`.`user_type`
-- -----------------------------------------------------
START TRANSACTION;
USE `periodika`;
INSERT INTO `periodika`.`user_type` (`id`, `name`) VALUES (1, 'admin');
INSERT INTO `periodika`.`user_type` (`id`, `name`) VALUES (2, 'client');

COMMIT;


-- -----------------------------------------------------
-- Data for table `periodika`.`user`
-- -----------------------------------------------------
START TRANSACTION;
USE `periodika`;
INSERT INTO `periodika`.`user` (`id`, `user_type_id`, `first_name`, `middle_name`, `last_name`, `email`, `address`, `phone`, `login`, `password`) VALUES (1, 1, 'John', '', 'Smith', 'email@domain.com.ua', 'address1', '+38(055)555-5555', 'admin', '');
INSERT INTO `periodika`.`user` (`id`, `user_type_id`, `first_name`, `middle_name`, `last_name`, `email`, `address`, `phone`, `login`, `password`) VALUES (2, 2, 'Іван', 'Сидорович', 'Петренко', 'my.email@domain.com', 'address2', '+38(066)666-6666', 'client', '');

COMMIT;


-- -----------------------------------------------------
-- Data for table `periodika`.`periodical`
-- -----------------------------------------------------
START TRANSACTION;
USE `periodika`;
INSERT INTO `periodika`.`periodical` (`id`, `title`, `description`, `periodical_category_id`, `min_subscription_period`, `issues_per_period`, `price_per_period`) VALUES (1, 'Газета по-українськи', ' Головні новини України та світу. Розділи «Події», «Оцінки», «Люди і речі» пропонують різні точки зору на основні соціально-політичні проблеми. Актуальні інтерв\'ю з цікавими людьми та змістовні репортажі. Новини культурного життя. Історія. Цікаві подорожі', 1, 1, 4, 56.45);
INSERT INTO `periodika`.`periodical` (`id`, `title`, `description`, `periodical_category_id`, `min_subscription_period`, `issues_per_period`, `price_per_period`) VALUES (2, 'ГОЛОВБУХ', 'Щотижневе видання для бухгалтера. Серед інших видань вирізняється великою кількістю робочих ситуацій. Це означає, що Ви отримуєте конкретні відповіді на запитання, що виникають у роботі. Тобто заощаджуєте час на пошук потрібної інформації. Щотижня редакція готує для Вас огляд змін законодавства, враховуючи всі нюанси бухгалтерського та податкового обліку. Також Ви отримаєте: зразки заповнення звітності; підказки до заповнення податкових декларацій; поради ведення розрахунків з контрагентами; приклади з судової практики; інструкції по роботі з бухгалтерськими програмами; Завжди на шпальтах тижневика: проводки в прикладах та покрокові інструкції дій в конкретних робочих ситуаціях', 3, 1, 4, 630);
INSERT INTO `periodika`.`periodical` (`id`, `title`, `description`, `periodical_category_id`, `min_subscription_period`, `issues_per_period`, `price_per_period`) VALUES (3, 'FINANCIAL TIMES', 'Преміум онлайн доступ до впливової щоденної британської газети. Перший номер видання вийшов у січні 1888 року. Видання швидко завоювало довіру читачів, ставши «Біблією біржового маклера». Для того, щоб шанувальники з першого погляду могли розрізнити конкурентів, в 1893 році «Financial Times» починає друкуватися на папері оранжево-рожевого відтінку (salmon paper). З 70-х років XX століття «Financial Times» починає розширювати сферу охоплення читацької аудиторії в світовому масштабі. 1979 ознаменувався першим виходом газети в тираж у Франкфурті, 1989 — у Парижі, в 1985 - у Нью Йорку. У 1995 році побачив світ інтернет-ресурс FT.com. В 1997 році газета удостоюється найпрестижнішої нагороди у світі журналістики «Газета року» від «What The Papers Say». Наступного року отримує нагороду «Газета року» від Премії Британської преси. Сьогодні вона широко відома, як одна з найкращих газет не лише Великобританії, а й світу, що спеціалізується на загальних новинах, міжнародній комерційній діяльності, фінансових новинах, підтримуючи при цьому незалежну редакційну політику. Матеріали газети та коментарі мають сильний вплив на фінансові кола світу і на фінансову політику Британського уряду.', 3, 12, 312, 25775.70);
INSERT INTO `periodika`.`periodical` (`id`, `title`, `description`, `periodical_category_id`, `min_subscription_period`, `issues_per_period`, `price_per_period`) VALUES (4, 'KYIV POST', 'Kyiv Post є найстарішою щотижневою суспільно-політичною англомовною газетою в Україні. Газета висвітлює всі сфери життєдіяльності суспільства, включаючи політику, бізнес, спорт, культуру, а також приділяє увагу подіям у світі.', 1, 1, 4, 174.70);
INSERT INTO `periodika`.`periodical` (`id`, `title`, `description`, `periodical_category_id`, `min_subscription_period`, `issues_per_period`, `price_per_period`) VALUES (5, 'ВИСОКИЙ ЗАМОК', 'У газеті ви знайдете гострі політичні матеріали, об\'єктивність, неупередженість, чесний погляд на соціальні проблеми і явища, на людські долі, готовність захистити незахищених, прийти на допомогу. «Високий Замок» - це турбота про ваше здоров\'я і безпеку, про комфорт і затишок у вашій оселі, про добробут вашої сім\'ї, про якісне виховання і навчання дітей. «Високий Замок» - це можливість відпочити. Інтерв\'ю з зірками, світські новини, туристичні «мандрівки» за кордон, сенсації і скандали, ігри з читачами, гумор, мода, гороскоп та кросворди. А ще у кожному номері безкоштовний журнал «Замок TV»', 1, 1, 8, 106.55);
INSERT INTO `periodika`.`periodical` (`id`, `title`, `description`, `periodical_category_id`, `min_subscription_period`, `issues_per_period`, `price_per_period`) VALUES (6, 'ДЗЕРКАЛО ТИЖНЯ. УКРАЇНА', 'Український суспільно-політичний тижневик, одне з найвпливовіших аналітичних видань в Україні', 1, 1, 4, 97.50);
INSERT INTO `periodika`.`periodical` (`id`, `title`, `description`, `periodical_category_id`, `min_subscription_period`, `issues_per_period`, `price_per_period`) VALUES (7, 'УРЯДОВИЙ КУР\'ЄР', '\"Газета \"Урядовий кур\'єр\" - щоденне видання центральних органів виконавчої влади України. Засновник — Кабінет Міністрів України. \"Урядовий кур\'єр\" оперативно інформує про найголовніші події у політичному, економічному, громадському, культурному житті країни та світу. Як офіційна газета органів державної виконавчої влади України, \"Урядовий кур\'єр\" першим отримує найповнішу та ексклюзивну інформацію про діяльність Президента і Уряду України, друкує на своїх сторінках повні тексти законів України, Указів Президента, постанов та розпоряджень Кабінету Міністрів, нормативні документи міністерств і відомств та коментарі до них. Значна частина цих документів вступає у дію з дня публікації в газеті. Особливе місце в газеті займає правова тематика, матеріали якої розраховані не тільки на фахових юристів, державних службовців, а й на широкий читацький загал. Моніторинг законодавства, аналіз нових законопроектів, узагальнення судової практики, діяльність правоохоронних органів, поради професійних правників, огляд ринку юридичних послуг – ось лише головні напрямки, які охоплює цей блок. Окрім того, \"Урядовий кур’єр\" залишається чи не єдиною в Україні газетою, яка регулярно надає своїм читачам юридичні консультації, підготовлені фахівцями міністерств і відомств', 1, 1, 20, 149.00);
INSERT INTO `periodika`.`periodical` (`id`, `title`, `description`, `periodical_category_id`, `min_subscription_period`, `issues_per_period`, `price_per_period`) VALUES (8, 'ГОЛОС УКРАЇНИ', 'Голос України - є державним друкованим засобом масової інформації, визнаним провідним у сфері інформаційної діяльності, що має загальнодержавне значення. Газета офіційно оприлюднює закони України, нормативно-правові акти України,  їх роз\'яснення та іншу офіційну інформацію, всебічно висвітлює діяльність Верховної Ради України, депутатських фракцій і груп, комітетів, тимчасових спеціальних комісій і тимчасових слідчих  комісій, народних депутатів України, органів місцевого самоврядування. В газеті висвітлюють актуальні проблеми державотворення і життя суспільства, проблеми соціально-економічних перетворень та соціального захисту населення, питання зміцнення законності  та правопорядку, культурного життя, а також інші соціально-політичні проблеми України. Крім суто роботи парламенту, газета розповідає про становлення місцевого самоврядування, актуальні проблеми державотворення, про проблеми соціально-економічних перетворень та соціального захисту населення, про питання зміцнення правопорядку та законності, висвітлює культурне життя держави – власне, розмаїття тем визначається діяльністю комітетів ВР. За дорученням парламенту «Голос України» публікує закони, міжнародні договори, постанови та інші акти Верховної Ради, проекти нормативно-правових документів, які потребують негайного широкого народного обговорення', 1, 1, 20, 160);
INSERT INTO `periodika`.`periodical` (`id`, `title`, `description`, `periodical_category_id`, `min_subscription_period`, `issues_per_period`, `price_per_period`) VALUES (9, 'LE MONDE', 'Найпопулярніша французька щоденна газета новин в області внутрішньої і зовнішньої політики, економіки, культурного та громадського життя у Франції і за кордоном. Вважається, що газета відображає офіційну точку зору нинішнього французького уряду і помірно правих партій. За це Le Figaro піддається критиці з боку \"лівих\" видань.', 1, 1, 24, 3447.60);
INSERT INTO `periodika`.`periodical` (`id`, `title`, `description`, `periodical_category_id`, `min_subscription_period`, `issues_per_period`, `price_per_period`) VALUES (10, 'БИБЛИОТЕКА БАЛАНС', '«Бібліотека« Баланс» - це серія унікальних практичних посібників. Теми практичних посібників вибираються за результатами опитування бухгалтерів. Представлені актуальні своєчасні статті, комплексно і глибоко висвітлюють заявлену тему. Кожен практичний збірник присвячений окремій темі в галузі бухгалтерського обліку та оподаткування, найбільш актуальною у місяць його виходу з друку, комплексно і всебічно розкриває цю тему і містить: консультації з питань практичного застосування норм законодавства, підготовлені висококваліфікованими фахівцями редакції, бухгалтерами-практиками, юристами, фахівцями держорганів.', 3, 1, 2, 453.75);
INSERT INTO `periodika`.`periodical` (`id`, `title`, `description`, `periodical_category_id`, `min_subscription_period`, `issues_per_period`, `price_per_period`) VALUES (11, 'КОЛОСВІТ', 'Для дітей 6-12 років. Подорож країнами світу, розповідь про їх історію, традиції, культуру, винаходи і т.д.', 4, 12, 10, 504);
INSERT INTO `periodika`.`periodical` (`id`, `title`, `description`, `periodical_category_id`, `min_subscription_period`, `issues_per_period`, `price_per_period`) VALUES (12, '1000 СКАНВОРДОВ', 'Яскравий збірник сканвордів, кросвордів, головоломок і детективних загадок', 4, 1, 1, 24.25);
INSERT INTO `periodika`.`periodical` (`id`, `title`, `description`, `periodical_category_id`, `min_subscription_period`, `issues_per_period`, `price_per_period`) VALUES (13, 'АПТЕЧКА - БИБЛИОТЕЧКА', 'Цей журнал допоможе вам знайти внутрішню гармонію, красу, впевненість в собі і зміцнити здоров\'є. Окрема тема для кожного номера дозволить зібрати унікальну колекцію рецептів, необхідну в кожному будинку на всі випадки життя. Рецепти з народної скарбнички перевірені часом і сотнями тисяч читачів. Такі засоби доступні кожному і безпечні для дітей.', 4, 1, 1, 17.25);
INSERT INTO `periodika`.`periodical` (`id`, `title`, `description`, `periodical_category_id`, `min_subscription_period`, `issues_per_period`, `price_per_period`) VALUES (14, 'COSMOPOLITAN', 'Міжнародний жіночий журнал. Зміст включає в себе статті про взаємини і секс, здоров\'я, кар\'єру, самовдосконалення, відомих людей, а також моду і красу.', 2, 12, 12, 3813.90);
INSERT INTO `periodika`.`periodical` (`id`, `title`, `description`, `periodical_category_id`, `min_subscription_period`, `issues_per_period`, `price_per_period`) VALUES (15, 'ELLE', ' ', 2, 12, 12, 3664.50);
INSERT INTO `periodika`.`periodical` (`id`, `title`, `description`, `periodical_category_id`, `min_subscription_period`, `issues_per_period`, `price_per_period`) VALUES (16, 'ELLE DECOR', ' ', 2, 12, 12, 3678.60);
INSERT INTO `periodika`.`periodical` (`id`, `title`, `description`, `periodical_category_id`, `min_subscription_period`, `issues_per_period`, `price_per_period`) VALUES (17, 'GQ STYLE', 'GQ Style - це каталог практичних рекомендацій, які допоможуть в будь-якій ситуації - чи збираєтеся ви на урочисту вечерю, прем\'єру чи в екстремальну подорож. Це найсвіжіші дані про моду з усього світу і актуальні тенденції очима кращих стилістів.', 2, 6, 1, 186.60);
INSERT INTO `periodika`.`periodical` (`id`, `title`, `description`, `periodical_category_id`, `min_subscription_period`, `issues_per_period`, `price_per_period`) VALUES (18, 'ESQUIRE', 'Esquire (з англ. - «есквайр») - щомісячний журнал, заснований в 1933 році в США. Основні теми: культура і мистецтво, мода і стиль, бізнес і політика, технології і автомобілі, їжа і здоров\'я, персони і інтерв\'ю. Розумний журнал для розумних людей!', 2, 1, 1, 203.70);
INSERT INTO `periodika`.`periodical` (`id`, `title`, `description`, `periodical_category_id`, `min_subscription_period`, `issues_per_period`, `price_per_period`) VALUES (19, 'LINUX FORMAT', 'Журнал, повністю присвячений операційним системам сімейства Linux і вільному програмному забезпеченню.', 5, 1, 1, 490.50);
INSERT INTO `periodika`.`periodical` (`id`, `title`, `description`, `periodical_category_id`, `min_subscription_period`, `issues_per_period`, `price_per_period`) VALUES (20, 'WHAT HI-FI', NULL, 5, 12, 12, 6181.44 );
INSERT INTO `periodika`.`periodical` (`id`, `title`, `description`, `periodical_category_id`, `min_subscription_period`, `issues_per_period`, `price_per_period`) VALUES (21, 'WIRED', 'Щомісячний журнал, що видається в Сан-Франциско і Лондоні. Пише про вплив комп\'ютерних технологій на культуру, економіку і політику.', 5, 12, 12, 5000.00);
INSERT INTO `periodika`.`periodical` (`id`, `title`, `description`, `periodical_category_id`, `min_subscription_period`, `issues_per_period`, `price_per_period`) VALUES (22, '4 Х 4 CLUB', 'Первый российский офф-роуд журнал. Жизнь внедорожников и самые безжалостные тесты, календарь джиперских гонок и как в них участвовать, советы по приобретению, эксплуатации и тюнингу.', 5, 1, 1, 229.80 );
INSERT INTO `periodika`.`periodical` (`id`, `title`, `description`, `periodical_category_id`, `min_subscription_period`, `issues_per_period`, `price_per_period`) VALUES (23, 'DIGITAL PHOTO', NULL, 5, 12, 12, 5178.90);
INSERT INTO `periodika`.`periodical` (`id`, `title`, `description`, `periodical_category_id`, `min_subscription_period`, `issues_per_period`, `price_per_period`) VALUES (24, 'CAR', 'Популярний щомісячний британський автомобільний журнал. Найбільш відомою особливістю журналу є групові тест-драйви під назвою «Giant Test», ідея яких була розроблена виданням і вперше опублікована в 1970-і роки. Крім того, «Car» на постійній основі публікує інтерв\'ю із значущими фігурами в автомобільній промисловості, огляди нових моделей автомобілів, новини зі світу автоспорту і інші статті, пов\'язані з автомобялмі.', 5, 12, 12, 7268.00);
INSERT INTO `periodika`.`periodical` (`id`, `title`, `description`, `periodical_category_id`, `min_subscription_period`, `issues_per_period`, `price_per_period`) VALUES (25, 'AEROSPACE AMERICA', NULL, 5, 12, 12, 8536.80);

COMMIT;


-- -----------------------------------------------------
-- Data for table `periodika`.`payment`
-- -----------------------------------------------------
START TRANSACTION;
USE `periodika`;
INSERT INTO `periodika`.`payment` (`id`, `date`, `sum`) VALUES (1, '2019-01-12 15:35:23', 268);

COMMIT;


-- -----------------------------------------------------
-- Data for table `periodika`.`subscription`
-- -----------------------------------------------------
START TRANSACTION;
USE `periodika`;
INSERT INTO `periodika`.`subscription` (`id`, `user_id`, `periodical_id`, `payment_id`, `period_start`, `period_count`, `period_end`, `quantity`, `sum`) VALUES (1, 1, 1, NULL, '2018-12-01', 6, '2019-11-01', 1, 0);
INSERT INTO `periodika`.`subscription` (`id`, `user_id`, `periodical_id`, `payment_id`, `period_start`, `period_count`, `period_end`, `quantity`, `sum`) VALUES (2, 1, 2, NULL, '2019-02-01', 3, '2019-11-01', 1, 0);
INSERT INTO `periodika`.`subscription` (`id`, `user_id`, `periodical_id`, `payment_id`, `period_start`, `period_count`, `period_end`, `quantity`, `sum`) VALUES (3, 1, 3, 1, '2018-12-01', 3, '2019-11-01', 2, 196);
INSERT INTO `periodika`.`subscription` (`id`, `user_id`, `periodical_id`, `payment_id`, `period_start`, `period_count`, `period_end`, `quantity`, `sum`) VALUES (4, 1, 1, 1, '2018-12-01', 1, '2018-12-01', 1, 72);
INSERT INTO `periodika`.`subscription` (`id`, `user_id`, `periodical_id`, `payment_id`, `period_start`, `period_count`, `period_end`, `quantity`, `sum`) VALUES (5, 1, 3, NULL, '2019-02-01', 3, '2019-01-01', 1, 0);

COMMIT;

-- begin attached script 'script'

-- end attached script 'script'
