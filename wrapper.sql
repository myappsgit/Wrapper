
--
-- Current Database: `wrapper`
--
CREATE DATABASE  IF NOT EXISTS `wrapper` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `wrapper`;
-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: 198.1.85.118    Database: wrapper
-- ------------------------------------------------------
-- Server version	5.7.21-0ubuntu0.16.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `account_device`
--

DROP TABLE IF EXISTS `account_device`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `account_device` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userId` int(11) NOT NULL,
  `deviceName` varchar(45) NOT NULL,
  `deviceType` varchar(45) NOT NULL,
  `macAddress` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `device_user_idx` (`userId`),
  CONSTRAINT `usr_device` FOREIGN KEY (`userId`) REFERENCES `user_details` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `account_notification`
--

DROP TABLE IF EXISTS `account_notification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `account_notification` (
  `userId` int(11) NOT NULL,
  `promotionToDevice` tinyint(1) DEFAULT '0',
  `promotionThruMail` tinyint(1) DEFAULT '0',
  `licenseExpiry` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`userId`),
  CONSTRAINT `noti_user` FOREIGN KEY (`userId`) REFERENCES `user_details` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `authorization`
--

DROP TABLE IF EXISTS `authorization`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `authorization` (
  `userId` int(11) NOT NULL,
  `password` varbinary(64) NOT NULL,
  `salt` varbinary(64) NOT NULL,
  PRIMARY KEY (`userId`),
  CONSTRAINT `auth_user` FOREIGN KEY (`userId`) REFERENCES `user_details` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `authorization`
--

LOCK TABLES `authorization` WRITE;
/*!40000 ALTER TABLE `authorization` DISABLE KEYS */;
INSERT INTO `authorization` VALUES (1,'\Ŝԓ\0Ҷô?�?֜ܟƧ,'0\ְ\Z\Ǧv\ܞqy\ݵsy0ȣθkΛ\἖񜇿L'),(2,'ȗٽќrh{̲\ㇼF񧬧GŀRqÓɿ,Ŝ'dțY\桤ІǭąS^܇Ep');
/*!40000 ALTER TABLE `authorization` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `default_settings`
--

DROP TABLE IF EXISTS `default_settings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `default_settings` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `maxDeviceCnt` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `default_settings`
--

LOCK TABLES `default_settings` WRITE;
/*!40000 ALTER TABLE `default_settings` DISABLE KEYS */;
INSERT INTO `default_settings` VALUES (1,5),(2,2147483647);
/*!40000 ALTER TABLE `default_settings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oauth_access_token`
--

DROP TABLE IF EXISTS `oauth_access_token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `oauth_access_token` (
  `token_id` varchar(256) DEFAULT NULL,
  `token` blob,
  `authentication_id` varchar(256) DEFAULT NULL,
  `user_name` varchar(256) DEFAULT NULL,
  `client_id` varchar(256) DEFAULT NULL,
  `authentication` blob,
  `refresh_token` varchar(256) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `oauth_client_details`
--

DROP TABLE IF EXISTS `oauth_client_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `oauth_client_details` (
  `userId` int(11) NOT NULL,
  `client_id` varchar(256) NOT NULL,
  `resource_ids` varchar(256) NOT NULL DEFAULT 'rest-api',
  `client_secret` varchar(256) NOT NULL,
  `scope` varchar(256) NOT NULL DEFAULT 'trust,read,write',
  `authorized_grant_types` varchar(256) NOT NULL DEFAULT 'password,authorization_code,refresh_token,implicit',
  `access_token_validity` int(11) NOT NULL DEFAULT '500',
  `refresh_token_validity` int(11) NOT NULL DEFAULT '1000',
  PRIMARY KEY (`client_id`),
  KEY `oauth_user_details_idx` (`userId`),
  CONSTRAINT `oauth_user_details` FOREIGN KEY (`userId`) REFERENCES `user_details` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `oauth_refresh_token`
--

DROP TABLE IF EXISTS `oauth_refresh_token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `oauth_refresh_token` (
  `token_id` varchar(256) DEFAULT NULL,
  `token` blob,
  `authentication` blob
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `package`
--

DROP TABLE IF EXISTS `package`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `package` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `productId` int(11) DEFAULT NULL,
  `name` varchar(45) DEFAULT NULL,
  `licenseType` varchar(45) DEFAULT NULL,
  `description` varchar(45) DEFAULT NULL,
  `icon` blob,
  PRIMARY KEY (`id`),
  KEY `pack_prod_idx` (`productId`),
  CONSTRAINT `pack_prod` FOREIGN KEY (`productId`) REFERENCES `product` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `package`
--

LOCK TABLES `package` WRITE;
/*!40000 ALTER TABLE `package` DISABLE KEYS */;
INSERT INTO `package` VALUES (1,1,'Default',NULL,NULL,NULL);
/*!40000 ALTER TABLE `package` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payment`
--

DROP TABLE IF EXISTS `payment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `payment` (
  `id` varchar(50) NOT NULL,
  `userId` int(11) NOT NULL,
  `productId` int(11) NOT NULL,
  `buyerName` varchar(45) NOT NULL,
  `method` varchar(45) NOT NULL,
  `amount` double NOT NULL,
  `status` varchar(45) NOT NULL,
  `fees` double NOT NULL,
  `paymentDate` date NOT NULL,
  PRIMARY KEY (`id`),
  KEY `payment_user_id_idx` (`userId`),
  KEY `payment_product_id_idx` (`productId`),
  CONSTRAINT `payment_product_id` FOREIGN KEY (`productId`) REFERENCES `product` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `payment_user_id` FOREIGN KEY (`userId`) REFERENCES `user_details` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `photo`
--

DROP TABLE IF EXISTS `photo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `photo` (
  `userId` int(11) NOT NULL,
  `image` mediumblob NOT NULL,
  PRIMARY KEY (`userId`),
  CONSTRAINT `photo_user` FOREIGN KEY (`userId`) REFERENCES `user_details` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `product` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `description` varchar(45) DEFAULT NULL,
  `icon` blob,
  `downloads` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` VALUES (1,'Cashup',NULL,NULL,0),(2,'Huddil',NULL,NULL,0);
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `refund`
--

DROP TABLE IF EXISTS `refund`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `refund` (
  `id` int(11) NOT NULL,
  `paymentId` varchar(50) NOT NULL,
  `status` varchar(45) NOT NULL,
  `totalAmount` double NOT NULL,
  `refundAmount` double NOT NULL,
  `refundDate` date NOT NULL,
  PRIMARY KEY (`id`),
  KEY `refund_payment_id_idx` (`paymentId`),
  CONSTRAINT `refund_payment_id` FOREIGN KEY (`paymentId`) REFERENCES `payment` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `state`
--

DROP TABLE IF EXISTS `state`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `state` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `state`
--

LOCK TABLES `state` WRITE;
/*!40000 ALTER TABLE `state` DISABLE KEYS */;
INSERT INTO `state` VALUES (1,'All'),(2,'Andaman and Nicobar Islands'),(3,'Andhra Pradesh'),(4,'Arunachal Pradesh'),(5,'Assam'),(6,'Bihar'),(7,'Chandigarh'),(8,'Chhattisgarh'),(9,'Dadra and Nagar Haveli'),(10,'Daman and Diu'),(11,'Delhi'),(12,'Goa'),(13,'Gujarat'),(14,'Haryana'),(15,'Himachal Pradesh'),(16,'Jammu and Kashmir'),(17,'Jharkhand'),(18,'Karnataka'),(19,'Kerala'),(20,'Lakshadweep'),(21,'Madhya Pradesh'),(22,'Maharashtra'),(23,'Manipur'),(24,'Meghalaya'),(25,'Mizoram'),(26,'Nagaland'),(27,'Odisha'),(28,'Puducherry'),(29,'Punjab'),(30,'Rajasthan'),(31,'Sikkim'),(32,'Tamil Nadu'),(33,'Telangana'),(34,'Tripura'),(35,'Uttar Pradesh'),(36,'Uttarakhand'),(37,'West Bengal');
/*!40000 ALTER TABLE `state` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `subscription`
--

DROP TABLE IF EXISTS `subscription`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `subscription` (
  `subID` int(11) NOT NULL AUTO_INCREMENT,
  `packageId` int(11) DEFAULT NULL,
  `subName` varchar(45) NOT NULL,
  `description` longtext,
  `price` decimal(4,2) NOT NULL,
  `type` varchar(45) NOT NULL,
  PRIMARY KEY (`subID`),
  KEY `sub_package_idx` (`packageId`),
  CONSTRAINT `sub_package` FOREIGN KEY (`packageId`) REFERENCES `package` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `terms_conditions`
--

DROP TABLE IF EXISTS `terms_conditions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `terms_conditions` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(45) NOT NULL,
  `description` longtext NOT NULL,
  `status` tinyint(1) NOT NULL DEFAULT '0',
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `userType` int(11) NOT NULL,
  `productId` int(11) NOT NULL DEFAULT '1',
  `startDate` date NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_type_idx` (`userType`),
  KEY `tcps_product_idx` (`productId`),
  CONSTRAINT `tcps_product` FOREIGN KEY (`productId`) REFERENCES `product` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `user_type` FOREIGN KEY (`userType`) REFERENCES `user_type` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `terms_conditions`
--

LOCK TABLES `terms_conditions` WRITE;
/*!40000 ALTER TABLE `terms_conditions` DISABLE KEYS */;
INSERT INTO `terms_conditions` VALUES (0,'','sample TCPS for advisor',1,'2018-01-04 06:59:33',6,2,'2018-01-03'),(1,' ','<style>.privacyHeading {    color: #004DB7;    text-align: center;    font-size: 28px;}.privacyAbout {    color: #004DB7;    text-align: justify;    font-size: 28px;    font-family: sans-serif;}.paragaphContnet {    font-size: 14px;    text-align: justify;    font-family: sans-serif;}.privacyContainer {    background-color: white;    padding: 25px;}</style><div class=\"privacyContainer\">    <h2 class=\"privacyHeading\">TERMS and CONDITIONS</h2>    <p>Terms and Conditions apply to a users’ (“User/Your�?) access to, and use of, www.huddil.com (the “Website�?). </p>    <p>These terms and conditions, are subjected to change from time to time, shall apply to all our services directly or indirectly (through distributors) made available online, through any mobile device, by email or by telephone. By accessing, browsing        and using our (mobile) website or any of our applications through whatever platform (hereafter collectively referred to as the \"website\") and/or by completing a reservation, you acknowledge and agree to have read, understood and agreed to the        terms and conditions set out below (including the privacy statement).</p>    <h4>Scope of our Service</h4>    <p>Through our website, we (MyApps) facilitate online service through which service provider can list their facilities (work spaces, office spaces, conference rooms, meeting rooms etc ) and consumer can avail these facilities.</p>    <p>The Website is operated by MyApps and their business subsidiaries’ a condition of using the Website and the services provided therein (“Services�?), you agree to be bound by these Terms. If you do not agree to these Terms, do not use this Website.    </p>    <p>It is mandatory that you review the Terms carefully before accessing or using the Website. These Terms are not intended to alter in any way or limit the terms or conditions of any other agreement that you may have with MyApps.</p>    <p>You acknowledge that MyApps is not the owner or operator of the Facilities, and its obligation is limited to facilitating the availability of the Website. You acknowledge that the MyApps is merely an aggregator of Merchants (“Space Providers�?) who        wish to advertise their Properties and services to potential customers. </p>    <p>While listing our Service, the information that we disclose is based on the information provided to us by Service Providers. As such, the Service Provider shall follow different path to update facility details and they are fully responsible for updating        information like rates, offers, availability and other information related facilities which is displayed on our Website.</p>    <h4>Age Limit</h4>    <p>Use of the Website is available only to persons over the age of eighteen (18). If you are a minor i.e. under the age of eighteen (18) years, you shall not register as a member of the Website and shall not sell or purchase any items on the Website.        We reserve the right to terminate your membership and refuse to provide you with access to the Website if it is brought to our notice or if it is discovered that you are under the age of eighteen (18) years. </p>    <h4>Accuracy in During Signup</h4>    <h5>USER ACCOUNT</h5>    <p>In order to use the Website, you are required to provide various information about yourself including your name, email address and other personal information. You agree that any information you provide to MyApps on the Website will always be accurate,        correct and up to date. You shall not impersonate someone else or provide account information, an email address or any other information that is not your own. </p>    <h5>PAYMENT </h5>    <p>While transacting payment on the Website, MyApps will not be responsible or assume any liability, whatsoever in respect of any loss or damage arising directly or indirectly to you due to (a) Lack of authorization for any transaction (b) Exceeding        the pre-set limit mutually agreed by you and between your bank (c) Any payment issues arising out of the transaction, or (d) Decline of transaction for any other reason. </p>    <p>All payments shall be made by you against valid orders placed by you for the tenure of the facility booked by you at the service provider’s location in case if it is offline payment. Use of the space booked by you shall be subject to you further complying        with terms of use specified by the Facility Owner. Mere payment of confirmation by MyApps does not guarantee the right to admission at the Facility premises. </p>    <p>The price, use of space, provision of amenities and other facilities etc are contractual obligations between the Merchant and you and merely using our website for transacting and completing the booking mechanism does not in any way make us liable        for denial of any services by the Facility Owner or for non-provision of any of the amenities or facilities whether in part or full. </p>    <h4>OWNERSHIP OF THE WEBSITE</h4>    <p>Website contains content owned or licensed by MyApps (“MyApps Content�?). MyApps owns and retains all rights in the MyApps Content. You will not remove, alter or conceal any copyright, trademark, service mark or other proprietary rights notices incorporated        in or accompanying the MyApps Content and you will not reproduce, modify, adapt, prepare derivative works based on, perform, display, publish, distribute, transmit, broadcast, sell, license or otherwise exploit the MyApps Content.    </p>    <p>The website name and logo are trademarks of MyApps, and may not be copied, imitated or used, in whole or in part, without the prior written permission of MyApps, except with prior consent. In addition, all custom graphics, button icons and scripts        are service marks, trademarks and/or trade dress of MyApps, and may not be copied, imitated or used, in whole or in part, without prior written permission from MyApps. </p>    <h4>USE OF THE WEBSITE AND CONDUCT </h4>    <p>You may use the Website for legitimate purposes only. You shall not post or transmit through the Website any material which violates or infringes the rights of others, or which is threatening, abusive, defamatory, libellous, invasive of privacy or        publicity rights, vulgar, obscene, profane or otherwise objectionable, contains injurious formulas, recipes, or instructions, which encourages conduct that would constitute a criminal offense, give rise to civil liability or otherwise violate        any law</p>    <p>You agree not to engage in any of the following prohibited activities: </p>    <ul>        <li>Copying, distributing, or disclosing any part of the Website in any medium;</li>        <li>Transmitting spam, chain letters, or other unsolicited email; </li>        <li>Attempting to interfere with, compromise the system integrity or security or decipher any transmissions to or from the servers running the Website; </li>        <li>Taking any action that imposes, or may impose at our sole discretion an unreasonable or disproportionately large load on our infrastructure; </li>        <li>Uploading invalid data, viruses, worms, or other software agents through the Website; </li>        <li>Collecting or harvesting any personally identifiable information, including account names, from the Website; </li>        <li>Using the Website for any commercial purposes without having all necessary rights and licenses to the Host Content;        </li>        <li>Impersonating another person or otherwise misrepresenting your affiliation with a person or entity, conducting fraud, hiding or attempting to hide your identity; </li>        <li>Interfering with the proper working of the Website; </li>        <li>Accessing any content on the Website through any technology or means other than those capabilities provided by the Website;        </li>        <li>By passing the measures we may use to prevent or restrict access to the Website, including without limitation features that prevent or restrict use or copying of any content or enforce limitations on use of the Website or the content therein.        </li>    </ul>    <h5>Hosting Content</h5>    <p>If you are a Service Provider, this clause is, for the avoidance of doubt, subject to the terms of the Service Provider Agreement. Any material you upload to our Site will be considered non-confidential and non-proprietary, and we have the right to        use, copy, distribute and disclose to third parties any such material for any purpose. We also have the right to disclose your identity to any third party who is claiming that any material posted or uploaded by you to our Site constitutes a violation        of their intellectual property rights, or of their right to privacy.</p>    <p>We will not be responsible, or liable to any third party, for the content or accuracy of any materials posted by you or any other user of our Site. We have the right to remove any material or posting you make on our Site if, in our opinion, such material        does not comply with the content standard stated below. </p>    <p>These content standards apply to all material which you contribute to our Site (“Contributions�?), including, for the avoidance of doubt, any material provided by a Service Provider and to any Interactive Services associated with it. You must comply        with the spirit of the following standards as well as the letter. The standards apply to each part of any Contribution as well as to its whole. You warrant that any such Contribution does comply with these standards, and you indemnify us for any        breach of that warranty. Contributions must:</p>    <ul>        <li>Be accurate (where they state facts).</li>        <li>Be genuinely held (where they state opinions).</li>        <li>Comply with applicable law in the Ireland and in any country from which they are posted.</li>    </ul>    <h5>Contributions must not:</h5>    <ul>        <li>Contain any material which is defamatory of any person.</li>        <li>Contain any material which is obscene, offensive, hateful or inflammatory.</li>        <li>Promote sexually explicit material.</li>        <li>Promote violence.</li>        <li>Promote discrimination based on race, sex, religion, nationality, disability, sexual orientation or age.</li>        <li>Infringe any copyright, database right or trade mark of any other person.</li>        <li>Be likely to deceive any person.</li>        <li>Be made in breach of any legal duty owed to a third party, such as a contractual duty or a duty of confidence.</li>        <li>Promote any illegal activity.</li>        <li>Be threatening, abuse or invade another\'s privacy, or cause annoyance, inconvenience or needless anxiety.</li>        <li>Be likely to harass, upset, embarrass, alarm or annoy any other person.</li>        <li>Be used to impersonate any person, or to misrepresent your identity or affiliation with any person.</li>        <li>Give the impression that they emanate from us, if this is not the case.</li>    </ul>    <h4>Address Section</h4>    <p> Corporate Office</p>    <p>4th floor, DSR Galeria BUILDING, </p>    <p>Bilekhahalli Gate, </p>    <p>Bannerghatta Road, Bangalore 76</p>    <p>We may change the Terms or modify any features of the Website at any time at our sole discretion. The most current version of the Terms can be viewed by clicking on the “Terms and Conditions�? on the website. If you continue to use the Website after        changes are posted, you will be deemed to have accepted the change. </p></div><app-footer></app-footer>',1,'2018-02-06 17:40:07',7,2,'2018-02-02'),(2,' ','<style>.privacyHeading {    color: #004DB7;    text-align: center;    font-size: 28px;}.privacyAbout {    color: #004DB7;    text-align: justify;    font-size: 28px;    font-family: sans-serif;}.paragaphContnet {    font-size: 14px;    text-align: justify;    font-family: sans-serif;}.privacyContainer {    background-color: white;    padding: 25px;}</style><div class=\"privacyContainer\">    <h2 class=\"privacyHeading\">TERMS and CONDITIONS</h2>    <p>Terms and Conditions apply to a users’ (“User/Your�?) access to, and use of, www.huddil.com (the “Website�?). </p>    <p>These terms and conditions, are subjected to change from time to time, shall apply to all our services directly or indirectly (through distributors) made available online, through any mobile device, by email or by telephone. By accessing, browsing        and using our (mobile) website or any of our applications through whatever platform (hereafter collectively referred to as the \"website\") and/or by completing a reservation, you acknowledge and agree to have read, understood and agreed to the        terms and conditions set out below (including the privacy statement).</p>    <h4>Scope of our Service</h4>    <p>Through our website, we (MyApps) facilitate online service through which service provider can list their facilities (work spaces, office spaces, conference rooms, meeting rooms etc ) and consumer can avail these facilities.</p>    <p>The Website is operated by MyApps and their business subsidiaries’ a condition of using the Website and the services provided therein (“Services�?), you agree to be bound by these Terms. If you do not agree to these Terms, do not use this Website.    </p>    <p>It is mandatory that you review the Terms carefully before accessing or using the Website. These Terms are not intended to alter in any way or limit the terms or conditions of any other agreement that you may have with MyApps.</p>    <p>You acknowledge that MyApps is not the owner or operator of the Facilities, and its obligation is limited to facilitating the availability of the Website. You acknowledge that the MyApps is merely an aggregator of Merchants (“Space Providers�?) who        wish to advertise their Properties and services to potential customers. </p>    <p>While listing our Service, the information that we disclose is based on the information provided to us by Service Providers. As such, the Service Provider shall follow different path to update facility details and they are fully responsible for updating        information like rates, offers, availability and other information related facilities which is displayed on our Website.</p>    <h4>Age Limit</h4>    <p>Use of the Website is available only to persons over the age of eighteen (18). If you are a minor i.e. under the age of eighteen (18) years, you shall not register as a member of the Website and shall not sell or purchase any items on the Website.        We reserve the right to terminate your membership and refuse to provide you with access to the Website if it is brought to our notice or if it is discovered that you are under the age of eighteen (18) years. </p>    <h4>Accuracy in During Signup</h4>    <h5>USER ACCOUNT</h5>    <p>In order to use the Website, you are required to provide various information about yourself including your name, email address and other personal information. You agree that any information you provide to MyApps on the Website will always be accurate,        correct and up to date. You shall not impersonate someone else or provide account information, an email address or any other information that is not your own. </p>    <h5>PAYMENT </h5>    <p>While transacting payment on the Website, MyApps will not be responsible or assume any liability, whatsoever in respect of any loss or damage arising directly or indirectly to you due to (a) Lack of authorization for any transaction (b) Exceeding        the pre-set limit mutually agreed by you and between your bank (c) Any payment issues arising out of the transaction, or (d) Decline of transaction for any other reason. </p>    <p>All payments shall be made by you against valid orders placed by you for the tenure of the facility booked by you at the service provider’s location in case if it is offline payment. Use of the space booked by you shall be subject to you further complying        with terms of use specified by the Facility Owner. Mere payment of confirmation by MyApps does not guarantee the right to admission at the Facility premises. </p>    <p>The price, use of space, provision of amenities and other facilities etc are contractual obligations between the Merchant and you and merely using our website for transacting and completing the booking mechanism does not in any way make us liable        for denial of any services by the Facility Owner or for non-provision of any of the amenities or facilities whether in part or full. </p>    <h4>OWNERSHIP OF THE WEBSITE</h4>    <p>Website contains content owned or licensed by MyApps (“MyApps Content�?). MyApps owns and retains all rights in the MyApps Content. You will not remove, alter or conceal any copyright, trademark, service mark or other proprietary rights notices incorporated        in or accompanying the MyApps Content and you will not reproduce, modify, adapt, prepare derivative works based on, perform, display, publish, distribute, transmit, broadcast, sell, license or otherwise exploit the MyApps Content.    </p>    <p>The website name and logo are trademarks of MyApps, and may not be copied, imitated or used, in whole or in part, without the prior written permission of MyApps, except with prior consent. In addition, all custom graphics, button icons and scripts        are service marks, trademarks and/or trade dress of MyApps, and may not be copied, imitated or used, in whole or in part, without prior written permission from MyApps. </p>    <h4>USE OF THE WEBSITE AND CONDUCT </h4>    <p>You may use the Website for legitimate purposes only. You shall not post or transmit through the Website any material which violates or infringes the rights of others, or which is threatening, abusive, defamatory, libellous, invasive of privacy or        publicity rights, vulgar, obscene, profane or otherwise objectionable, contains injurious formulas, recipes, or instructions, which encourages conduct that would constitute a criminal offense, give rise to civil liability or otherwise violate        any law</p>    <p>You agree not to engage in any of the following prohibited activities: </p>    <ul>        <li>Copying, distributing, or disclosing any part of the Website in any medium;</li>        <li>Transmitting spam, chain letters, or other unsolicited email; </li>        <li>Attempting to interfere with, compromise the system integrity or security or decipher any transmissions to or from the servers running the Website; </li>        <li>Taking any action that imposes, or may impose at our sole discretion an unreasonable or disproportionately large load on our infrastructure; </li>        <li>Uploading invalid data, viruses, worms, or other software agents through the Website; </li>        <li>Collecting or harvesting any personally identifiable information, including account names, from the Website; </li>        <li>Using the Website for any commercial purposes without having all necessary rights and licenses to the Host Content;        </li>        <li>Impersonating another person or otherwise misrepresenting your affiliation with a person or entity, conducting fraud, hiding or attempting to hide your identity; </li>        <li>Interfering with the proper working of the Website; </li>        <li>Accessing any content on the Website through any technology or means other than those capabilities provided by the Website;        </li>        <li>By passing the measures we may use to prevent or restrict access to the Website, including without limitation features that prevent or restrict use or copying of any content or enforce limitations on use of the Website or the content therein.        </li>    </ul>    <h5>Hosting Content</h5>    <p>If you are a Service Provider, this clause is, for the avoidance of doubt, subject to the terms of the Service Provider Agreement. Any material you upload to our Site will be considered non-confidential and non-proprietary, and we have the right to        use, copy, distribute and disclose to third parties any such material for any purpose. We also have the right to disclose your identity to any third party who is claiming that any material posted or uploaded by you to our Site constitutes a violation        of their intellectual property rights, or of their right to privacy.</p>    <p>We will not be responsible, or liable to any third party, for the content or accuracy of any materials posted by you or any other user of our Site. We have the right to remove any material or posting you make on our Site if, in our opinion, such material        does not comply with the content standard stated below. </p>    <p>These content standards apply to all material which you contribute to our Site (“Contributions�?), including, for the avoidance of doubt, any material provided by a Service Provider and to any Interactive Services associated with it. You must comply        with the spirit of the following standards as well as the letter. The standards apply to each part of any Contribution as well as to its whole. You warrant that any such Contribution does comply with these standards, and you indemnify us for any        breach of that warranty. Contributions must:</p>    <ul>        <li>Be accurate (where they state facts).</li>        <li>Be genuinely held (where they state opinions).</li>        <li>Comply with applicable law in the Ireland and in any country from which they are posted.</li>    </ul>    <h5>Contributions must not:</h5>    <ul>        <li>Contain any material which is defamatory of any person.</li>        <li>Contain any material which is obscene, offensive, hateful or inflammatory.</li>        <li>Promote sexually explicit material.</li>        <li>Promote violence.</li>        <li>Promote discrimination based on race, sex, religion, nationality, disability, sexual orientation or age.</li>        <li>Infringe any copyright, database right or trade mark of any other person.</li>        <li>Be likely to deceive any person.</li>        <li>Be made in breach of any legal duty owed to a third party, such as a contractual duty or a duty of confidence.</li>        <li>Promote any illegal activity.</li>        <li>Be threatening, abuse or invade another\'s privacy, or cause annoyance, inconvenience or needless anxiety.</li>        <li>Be likely to harass, upset, embarrass, alarm or annoy any other person.</li>        <li>Be used to impersonate any person, or to misrepresent your identity or affiliation with any person.</li>        <li>Give the impression that they emanate from us, if this is not the case.</li>    </ul>    <h4>Address Section</h4>    <p> Corporate Office</p>    <p>4th floor, DSR Galeria BUILDING, </p>    <p>Bilekhahalli Gate, </p>    <p>Bannerghatta Road, Bangalore 76</p>    <p>We may change the Terms or modify any features of the Website at any time at our sole discretion. The most current version of the Terms can be viewed by clicking on the “Terms and Conditions�? on the website. If you continue to use the Website after        changes are posted, you will be deemed to have accepted the change. </p></div><app-footer></app-footer>',1,'2018-02-06 17:40:47',8,2,'2018-02-02');
/*!40000 ALTER TABLE `terms_conditions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `terms_conditions_history`
--

DROP TABLE IF EXISTS `terms_conditions_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `terms_conditions_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userId` int(11) NOT NULL,
  `terms_conditions_id` int(11) NOT NULL,
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `terms_conditions_id_name` (`terms_conditions_id`),
  KEY `terms_conditions` (`userId`),
  CONSTRAINT `terms_conditions` FOREIGN KEY (`userId`) REFERENCES `user_details` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `terms_conditions_id_name` FOREIGN KEY (`terms_conditions_id`) REFERENCES `terms_conditions` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tokens`
--

DROP TABLE IF EXISTS `tokens`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tokens` (
  `userId` int(11) NOT NULL,
  `token` varchar(45) DEFAULT NULL,
  `otp` varchar(6) DEFAULT NULL,
  PRIMARY KEY (`userId`),
  KEY `token_user_idx` (`userId`),
  CONSTRAINT `token_user` FOREIGN KEY (`userId`) REFERENCES `user_details` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_details`
--

DROP TABLE IF EXISTS `user_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_details` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `firstName` varchar(45) DEFAULT NULL,
  `lastName` varchar(45) DEFAULT NULL,
  `addressingName` varchar(45) NOT NULL,
  `userName` varchar(45) NOT NULL,
  `emailId` varchar(45) NOT NULL,
  `gender` varchar(1) DEFAULT NULL,
  `dob` date DEFAULT NULL,
  `age` tinyint(1) DEFAULT '0',
  `mobileNo` varchar(45) NOT NULL DEFAULT '0000000000',
  `address` text,
  `city` varchar(45) DEFAULT NULL,
  `country` varchar(45) DEFAULT NULL,
  `pincode` varchar(45) DEFAULT NULL,
  `website` varchar(128) DEFAULT NULL,
  `isActive` int(11) NOT NULL DEFAULT '0',
  `signedUp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `email_id_UNIQUE` (`emailId`),
  UNIQUE KEY `user_name_UNIQUE` (`userName`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='User Details';

/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_details`
--

LOCK TABLES `user_details` WRITE;
/*!40000 ALTER TABLE `user_details` DISABLE KEYS */;
INSERT INTO `user_details` VALUES (1,NULL,NULL,'admin','admin','admin@huddil.com',NULL,NULL,0,'+910000000001',NULL,NULL,NULL,NULL,NULL,1,'2017-11-20 12:38:26'),(2,NULL,NULL,'advisor','advisor@huddil.com','advisor@huddil.com',NULL,NULL,0,'+910000000002',NULL,NULL,NULL,NULL,NULL,1,'2017-11-20 12:40:48');
/*!40000 ALTER TABLE `user_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_session`
--

DROP TABLE IF EXISTS `user_session`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_session` (
  `sNo` int(11) NOT NULL AUTO_INCREMENT,
  `sessionId` varchar(128) NOT NULL,
  `userId` int(11) NOT NULL,
  `macAddress` varchar(45) NOT NULL,
  `loggedIn` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `isActive` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`sNo`,`sessionId`),
  UNIQUE KEY `userId_UNIQUE` (`userId`),
  UNIQUE KEY `sessionId_UNIQUE` (`sessionId`),
  KEY `usr_session_idx` (`userId`),
  KEY `usr_sessionId_idx` (`sessionId`),
  CONSTRAINT `usr_session` FOREIGN KEY (`userId`) REFERENCES `user_details` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_subscription`
--

DROP TABLE IF EXISTS `user_subscription`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_subscription` (
  `sNo` int(11) NOT NULL AUTO_INCREMENT,
  `userId` int(11) NOT NULL,
  `subId` int(11) DEFAULT NULL,
  `dateOfSubscription` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `expiration` datetime DEFAULT NULL,
  `packageId` int(11) DEFAULT NULL,
  `productUserType` int(11) NOT NULL,
  PRIMARY KEY (`sNo`),
  KEY `user_sub_idx` (`subId`),
  KEY `usrsub_usr_idx` (`userId`),
  KEY `user_type_idx` (`productUserType`),
  KEY `usrsub_package_idx` (`packageId`),
  CONSTRAINT `product_user` FOREIGN KEY (`productUserType`) REFERENCES `user_type` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `usrsub_package` FOREIGN KEY (`packageId`) REFERENCES `package` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `usrsub_sub` FOREIGN KEY (`subId`) REFERENCES `subscription` (`subID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `usrsub_usr` FOREIGN KEY (`userId`) REFERENCES `user_details` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_subscription`
--

LOCK TABLES `user_subscription` WRITE;
/*!40000 ALTER TABLE `user_subscription` DISABLE KEYS */;
INSERT INTO `user_subscription` VALUES (1,1,NULL,'2017-11-20 12:38:26',NULL,NULL,5),(2,2,NULL,'2017-11-20 12:40:48',NULL,NULL,6);
/*!40000 ALTER TABLE `user_subscription` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_type`
--

DROP TABLE IF EXISTS `user_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `productId` int(11) NOT NULL,
  `type` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `product_id_idx` (`productId`),
  CONSTRAINT `product_id` FOREIGN KEY (`productId`) REFERENCES `product` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_type`
--

LOCK TABLES `user_type` WRITE;
/*!40000 ALTER TABLE `user_type` DISABLE KEYS */;
INSERT INTO `user_type` VALUES (1,1,'Advertiser'),(2,1,'Advisor'),(3,1,'Product Owner'),(4,1,'Consumer'),(5,2,'Administrator'),(6,2,'Advisor'),(7,2,'Service Provider'),(8,2,'Consumer');
/*!40000 ALTER TABLE `user_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wallet`
--

DROP TABLE IF EXISTS `wallet`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `wallet` (
  `userId` int(11) NOT NULL,
  `points` int(11) NOT NULL,
  PRIMARY KEY (`userId`),
  CONSTRAINT `wallet_user` FOREIGN KEY (`userId`) REFERENCES `user_details` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping events for database 'wrapper'
--

--
-- Dumping routines for database 'wrapper'
--
/*!50003 DROP PROCEDURE IF EXISTS `activateUser` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `activateUser`(IN p_emailId VARCHAR(45), IN p_token VARCHAR(45), OUT p_userId INT, OUT p_userType INT, OUT p_result INT)
BEGIN

    DECLARE isActivated INT;

    SELECT u.isActive INTO isActivated FROM user_details u WHERE u.emailId = p_emailId;
 
    IF(isActivated = 0) THEN
        SET p_result = 2;
    ELSEIF(isActivated = 2) THEN
        SET p_result = 3;
	ELSE 
		UPDATE user_details u JOIN tokens t ON t.userId = u.id SET u.isActive = 0 WHERE u.emailId = p_emailId AND t.token = p_token;
		SELECT ROW_COUNT() INTO p_result;
		IF(p_result = 1)THEN
			DELETE FROM tokens WHERE token = p_token;
			SELECT s.userId, s.productUserType INTO p_userId, p_userType FROM user_subscription s JOIN user_details u ON u.id = s.userId WHERE u.emailId = p_emailId;
		END IF;
   END IF;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `changeUserStatus` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `changeUserStatus`(IN p_emailId VARCHAR(45), IN p_token VARCHAR(45), OUT p_userId INT, OUT p_userType INT, OUT p_result INT)
BEGIN
	DECLARE v_oldStatus INT;
    DECLARE v_newStatus INT;
    SELECT isActive INTO v_oldStatus FROM user_details WHERE id = p_userId;
    IF(p_deActivateUser = TRUE) THEN
		IF(v_oldStatus = 0) THEN
			SET v_newStatus = 2;
            SET p_result = 1;
		ELSEIF(v_oldStatus = 1) THEN
			SET v_newStatus = 3;
            SET p_result = 1;
		ELSE
			SET p_result = 2;
        END IF;
	ELSE
		IF(v_oldStatus = 2) THEN
			SET v_newStatus = 0;
            SET p_result = 3;
		ELSEIF(v_oldStatus = 3) THEN
			SET v_newStatus = 1;
            SET p_result = 3;
		ELSE
			SET p_result = 4;
        END IF;
    END IF;
    IF(p_result = 1 || p_result = 3) THEN
		UPDATE user_details u SET u.isActive = v_newStatus WHERE id = p_userId;
    END IF;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `createSession` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `createSession`(IN p_emailId VARCHAR(45), INOUT p_sessionId VARCHAR(128), IN p_deviceName VARCHAR(45),
					IN p_deviceType VARCHAR(45), IN p_macAddress VARCHAR(45), IN p_product VARCHAR(50),
                    OUT p_userType INT, OUT p_userId INT, OUT p_result INT)
BEGIN
	/*
		result values
        0 - macAddress is associated with the differnt user
        1 - active user session exists
        2 - device limit reached
        3 - new user session is created for new device
        4 - new user session is created for existing device
    */
	DECLARE v_sessionId VARCHAR(128);
	DECLARE v_productId INT;
	DECLARE v_terms_conditions_id INT;
	DECLARE v_id INT;
	DECLARE v_tcps BOOLEAN;
	DECLARE v_appVersion BOOLEAN;
	DECLARE v_offers BOOLEAN;
	DECLARE v_messages MEDIUMTEXT;
	DECLARE v_name VARCHAR(45);
    
    IF(p_emailId <> '') THEN
		SELECT u.id INTO v_id FROM user_details u WHERE u.emailId = p_emailId;
        SET p_userId = v_id;
    END IF;
    
    IF(p_userId IS NULL) THEN
		SET p_result = -1;
    ELSE
		SELECT s.sessionId INTO v_sessionId FROM wrapper.user_session s 
				WHERE s.userId = p_userId AND s.isActive = 1;
		IF(v_sessionId IS NOT NULL) THEN
			SET p_result = 1;
			SET p_sessionId = v_sessionId;
		ELSE
			SET p_result = 4;
			INSERT INTO `wrapper`.`user_session`(`sessionId`, `userId`, `macAddress`)
				VALUES(p_sessionId, p_userId, macAddress);
		END IF;
		IF(p_product = 'cashup') THEN
			SELECT p.userType INTO p_userType FROM `cashup`.`user_preferences` p WHERE p.`userId` = p_userId;
			UPDATE `cashup`.`user_preferences` p SET p.`sessionId` = sessionId WHERE p.`userId` = p_userId;
			SELECT id INTO v_productId FROM product;
		ELSEIF(p_product = 'huddil') THEN
			SELECT productUserType INTO p_userType FROM `user_subscription` WHERE `userId` = p_userId;
			SELECT id INTO v_productId FROM product WHERE name = p_product;
		END IF;
		SELECT addressingName INTO v_name FROM user_details WHERE id = p_userId;
		SELECT id INTO v_id FROM wrapper.terms_conditions WHERE userType = p_userType AND status = 1 AND productId = v_productId ;
		SELECT terms_conditions_id INTO v_terms_conditions_id FROM wrapper.terms_conditions_history WHERE userId = p_userId ORDER BY date DESC LIMIT 0 ,1;
		IF(v_id = v_terms_conditions_id)THEN
			SET v_tcps = 0;
		ELSE
			SET v_tcps = 1;
		END IF;
		
		SET v_offers = 0;
		SET v_appVersion = 0;
		SELECT v_tcps AS tcps, v_appVersion AS appVersion, v_offers AS offers, 'no message' AS messages, v_name AS name;
    END IF;
	/*DECLARE v_userId INT;
    DECLARE v_count INT;
    DECLARE v_macAddress VARCHAR(45);
    DECLARE v_maxCount INT;
    
	SELECT d.userId INTO v_userId FROM wrapper.account_device d 
		WHERE d.macAddress = macAddress;
        
	IF (v_userId = userId OR v_userId IS NULL) THEN
		
        SELECT maxDeviceCnt INTO v_maxCount FROM default_settings WHERE id = 1;
        
		SELECT ad.macAddress , rs.c INTO v_macAddress, v_count FROM account_device AS ad 
		RIGHT JOIN(SELECT COUNT(d.macAddress) AS c FROM account_device d WHERE d.userId = userId) AS rs 
		ON ad.userId = userId AND ad.macAddress = macAddress;
        
        IF (v_count < v_maxCount + 1 AND v_macAddress IS NOT NULL) THEN
			SELECT s.sessionId INTO v_sessionId FROM wrapper.user_session s 
            WHERE s.macAddress = macAddress AND s.isActive = 1;
            IF(v_sessionId IS NOT NULL) THEN
				SET result = 1;
                SET sessionId = v_sessionId;
			ELSE
				SET result = 4;
				INSERT INTO `wrapper`.`user_session`(`sessionId`, `userId`, `macAddress`)
				VALUES(sessionId, userId, macAddress);
				UPDATE `cashup`.`user_preferences` p 
					SET	p.`sessionId` = sessionId WHERE p.`userId` = userId;
			END IF;
			SELECT p.userType INTO userType FROM `cashup`.`user_preferences` p
			WHERE p.`userId` = userId;

		ELSEIF (v_count < v_maxCount) THEN 
			SET result = 3;
			INSERT INTO `wrapper`.`account_device`(`userId`, `deviceName`, `deviceType`, `macAddress`)
			VALUES (userId, deviceName, deviceType, macAddress);
			INSERT INTO `wrapper`.`user_session`(`sessionId`, `userId`, `macAddress`)
			VALUES(sessionId, userId, macAddress);
			UPDATE `cashup`.`user_preferences` p 
				SET	p.`sessionId` = sessionId WHERE p.`userId` = userId;
            SELECT p.userType INTO userType FROM `cashup`.`user_preferences` p
				WHERE p.`userId` = userId;
		ELSE
			SET result = 2;
        END IF;
	ELSE
		SET result = 0;
    END IF;*/
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `createTCPS` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `createTCPS`(IN p_description TEXT, IN p_userType INT, OUT p_result TINYINT(1))
BEGIN

	DECLARE v_id INT;
	INSERT INTO `wrapper`.`terms_conditions` (`description`, `status`, `userType`) VALUES (p_description, TRUE, p_userType);
    SET v_id = ROW_COUNT();
    IF(v_id = 1) THEN
		SET v_id = LAST_INSERT_ID();
        UPDATE wrapper.terms_conditions 
			SET status = 0 WHERE userType = p_userType AND id <> v_id;
		SET p_result = TRUE;
	ELSE
		SET p_result = FALSE;
	END IF;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `dbCleanUp` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `dbCleanUp`()
BEGIN
	DECLARE v_date DATE;
    DECLARE v_id INT;
    DECLARE v_userType INT;
    SET v_date = CURDATE();
	UPDATE wrapper.user_details
		SET age = age + TIMESTAMPDIFF(YEAR, signedUp, v_date + 1);
	UPDATE cashup.tasks
		SET active = 4 WHERE endDate = v_date;
	UPDATE cashup.tasks
		SET active = 1 WHERE startDate <= v_date AND active = 5;
	SET @userTypes = '';
	SELECT GROUP_CONCAT(userType) INTO @userTypes FROM terms_conditions WHERE startDate = CURDATE();
	UPDATE terms_conditions SET status = 0 WHERE FIND_IN_SET(userType, @userTypes);
	UPDATE terms_conditions SET status = 1 WHERE startDate = CURDATE();
	IF(DATE_FORMAT(v_date, '%e') = 1)THEN
		DELETE FROM cashup.videos_to_delete;
		DELETE u FROM wrapper.user_details u 
			JOIN cashup.user_preferences p ON u.id = p.userId
			WHERE DATEDIFF(v_date, u.signedUp) > 30 AND u.isActive = 0 AND p.userType = 4;
		DELETE FROM cashup.tasks_for_approval
			WHERE DATEDIFF(v_date, updatedOn) > 30 AND status = 2;
		SELECT location FROM cashup.videos_to_delete;
    END IF;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `logout` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `logout`(IN sesId VARCHAR(128), OUT rowCnt INT)
BEGIN

UPDATE wrapper.user_session SET isActive = 0
WHERE sessionId = sesId;
SET rowCnt = ROW_COUNT();

IF(rowCnt = 1) THEN
	DELETE d FROM wrapper.account_device AS d JOIN wrapper.user_session AS s ON 
	d.macAddress = s.macAddress AND s.sessionId = sesId;
END IF;

END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-02-12 12:58:26