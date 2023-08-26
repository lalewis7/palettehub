CREATE TABLE `palette_hub`.`collections` (
  `collection_id` CHAR(32) NOT NULL,
  `user_id` CHAR(32) CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_0900_ai_ci' NOT NULL,
  `name` VARCHAR(64) CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_bin' NULL,
  PRIMARY KEY (`collection_id`),
  INDEX `user_id_fk_idx` (`user_id` ASC) VISIBLE,
  CONSTRAINT `user_id_fk`
    FOREIGN KEY (`user_id`)
    REFERENCES `palette_hub`.`users` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE TABLE `palette_hub`.`collection_palettes` (
  `collection_id` CHAR(32) NOT NULL,
  `palette_id` CHAR(32) CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_0900_ai_ci' NOT NULL,
  INDEX `collection_id_fk_idx` (`collection_id` ASC) VISIBLE,
  INDEX `palette_id_fk_idx` (`palette_id` ASC) VISIBLE,
  CONSTRAINT `collection_id_fk`
    FOREIGN KEY (`collection_id`)
    REFERENCES `palette_hub`.`collections` (`collection_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `palette_id_fk`
    FOREIGN KEY (`palette_id`)
    REFERENCES `palette_hub`.`palettes` (`palette_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

ALTER TABLE `palette_hub`.`users` 
ADD COLUMN `show_picture` TINYINT NULL DEFAULT 1 AFTER `email`;
ADD COLUMN `banner_color_1` CHAR(6) NULL DEFAULT '63beff' AFTER `show_picture`,
ADD COLUMN `banner_color_2` CHAR(6) NULL DEFAULT '7910b6' AFTER `banner_color_1`;
ADD COLUMN `role` VARCHAR(8) NULL DEFAULT 'user' AFTER `banner_color_2`;