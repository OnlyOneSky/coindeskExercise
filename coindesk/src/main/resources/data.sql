insert into bpi (currency_code, symbol, rate, rate_float, description, created, updated)
values ('USD','&#36;','44,084.7681',44084.7681,1, CURRENT_TIME, CURRENT_TIME);
insert into bpi (currency_code, symbol, rate, rate_float, description, created, updated)
values ('GBP', '&pound;', '36,836.8795', 36836.8795,1, CURRENT_TIME, CURRENT_TIME);
insert into bpi (currency_code, symbol, rate, rate_float, description, created, updated)
values ('EUR', '&euro;', '42,945.0005', 42945.0005,1, CURRENT_TIME, CURRENT_TIME);


insert into language (id, language) values ('en-US', 'English');
insert into language (id, language) values ('zh-TW', 'Chinese');


insert into currency (id, currency_code) values (1, 'USD');
insert into currency (id, currency_code) values (2, 'GBP');
insert into currency (id, currency_code) values (3, 'EUR');

insert into translation (language_id, text_column_id, translating_table_uid, translation)
values (1, 1, 1, 'United States Dollar');
insert into translation (language_id, text_column_id, translating_table_uid, translation)
values (1, 1, 2, 'British Pound Sterling');
insert into translation (language_id, text_column_id, translating_table_uid, translation)
values (1, 1, 3, 'Euro');
insert into translation (language_id, text_column_id, translating_table_uid, translation)
values (1, 2, 1, 'US test Name');
insert into translation (language_id, text_column_id, translating_table_uid, translation)
values (1, 2, 2, 'British test Name');
insert into translation (language_id, text_column_id, translating_table_uid, translation)
values (1, 2, 3, 'Euro test Name');
insert into translation (language_id, text_column_id, translating_table_uid, translation)
values (2, 1, 1, '美金');
insert into translation (language_id, text_column_id, translating_table_uid, translation)
values (2, 1, 2, '英鎊');
insert into translation (language_id, text_column_id, translating_table_uid, translation)
values (2, 1, 3, '歐元');
insert into translation (language_id, text_column_id, translating_table_uid, translation)
values (2, 2, 1, '美國測試名稱');
insert into translation (language_id, text_column_id, translating_table_uid, translation)
values (2, 2, 2, '英國測試名稱');
insert into translation (language_id, text_column_id, translating_table_uid, translation)
values (2, 2, 3, '歐洲測試名稱');

insert into text_column (id, column_name) values (1, 'currency_description');
insert into text_column (id, column_name) values (2, 'test_name');