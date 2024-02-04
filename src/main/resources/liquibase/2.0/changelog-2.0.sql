-- Populate USER_ table
INSERT INTO USER_ (ID_, USER_NAME_, PASSWORD_)
VALUES ('123e4567-e89b-12d3-a456-426614174001', 'username', '$2a$12$sv801IqB10Ic69WjlfyUqemCLZTGVbfBrbaX15VeharZknCsFngf6');

-- Populate CUSTOMER_ table
INSERT INTO CUSTOMER_ (ID_, NAME_, SURNAME_, BIRTHDATE_, GSM_NUMBER_, BALANCE_, USER_ID_)
VALUES ('123e4567-e89b-12d3-a456-426614175001', 'John', 'Doe', '1990-01-01', '994704344656', 100.00,
        '123e4567-e89b-12d3-a456-426614174001');