INSERT INTO meal_types(id, name) VALUES (nextval('SQ_MEALTYPE_SEQ'),'Завтрак');
INSERT INTO meal_types(id,name) VALUES (nextval('SQ_MEALTYPE_SEQ'),'Обед');
INSERT INTO meal_types(id,name) VALUES (nextval('SQ_MEALTYPE_SEQ'),'Ужин');

INSERT INTO dishes(id,name, cost) VALUES (nextval('SQ_DISH_SEQ'),'Борщ', 150);
INSERT INTO dishes(id,name, cost) VALUES (nextval('SQ_DISH_SEQ'),'Картофельное пюре', 50);
INSERT INTO dishes(id,name, cost) VALUES (nextval('SQ_DISH_SEQ'),'Куриная Котлета', 45);
INSERT INTO dishes(id,name, cost) VALUES (nextval('SQ_DISH_SEQ'),'Морс', 30.50);

