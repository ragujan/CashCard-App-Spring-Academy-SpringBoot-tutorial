-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               PostgreSQL 16.2, compiled by Visual C++ build 1937, 64-bit
-- Server OS:                    
-- HeidiSQL Version:             12.0.0.6468
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES  */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

-- Dumping structure for table public.cash_cards
CREATE TABLE IF NOT EXISTS "cash_cards" (
	"id" BIGINT NOT NULL,
	"amount" DOUBLE PRECISION NOT NULL,
	"owner" VARCHAR(255) NULL DEFAULT 'NULL::character varying',
	PRIMARY KEY ("id")
);

-- Dumping data for table public.cash_cards: 6 rows
/*!40000 ALTER TABLE "cash_cards" DISABLE KEYS */;
INSERT INTO "cash_cards" ("id", "amount", "owner") VALUES
	(1, 111.11, 'sarah1'),
	(4, 222.99, 'sarah1'),
	(8, 333.99, 'sarah1'),
	(5, 333.99, 'sarah1'),
	(6, 444.99, 'sarah1'),
	(9, 888.99, 'Kumar'),
	(13, 777.99, 'sarah1');
/*!40000 ALTER TABLE "cash_cards" ENABLE KEYS */;

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
