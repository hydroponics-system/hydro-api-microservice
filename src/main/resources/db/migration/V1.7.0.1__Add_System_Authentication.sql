-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
-- Script: V1.7.0.1__Add_System_Authentication.sql
-- Author: Sam Butler
-- Date: August 22, 2022
-- Issue: 
-- Version: v1.7.0
-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

-- ---------------------------------------------------------------------------------
-- HYDRO-4: START
-- ---------------------------------------------------------------------------------

ALTER TABLE systems DROP FOREIGN KEY user_profile__systems__FK1;
ALTER TABLE systems 
  DROP COLUMN insert_user_id, 
  ADD COLUMN password BINARY(60) NOT NULL AFTER name,
  DROP INDEX systems_IDX1;

ALTER TABLE systems
  ADD COLUMN owner_user_id INT UNSIGNED NULL AFTER password;

ALTER TABLE systems ADD CONSTRAINT user_profile__systems__FK1 
  FOREIGN KEY (owner_user_id) REFERENCES user_profile(id) 
    ON DELETE CASCADE 
    ON UPDATE CASCADE;

-- ---------------------------------------------------------------------------------
-- HYDRO-4: END
-- ---------------------------------------------------------------------------------

-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
-- END OF SCRIPT VERSION
