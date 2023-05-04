
/* Drop Tables */

DROP TABLE SAMPLE_CART_ITEMS CASCADE CONSTRAINTS;
DROP TABLE SAMPLE_ORDER_ITEMS CASCADE CONSTRAINTS;
DROP TABLE SAMPLE_POINT_HISTORIES CASCADE CONSTRAINTS;
DROP TABLE SAMPLE_ORDERS CASCADE CONSTRAINTS;
DROP TABLE SAMPLE_PRODUCTS CASCADE CONSTRAINTS;
DROP TABLE SAMPLE_USERS CASCADE CONSTRAINTS;




/* Create Tables */

CREATE TABLE SAMPLE_CART_ITEMS
(
	USER_NO number(4,0) NOT NULL,
	PRODUCT_NO number(4,0) NOT NULL,
	ITEM_AMOUNT number(3,0),
	CONSTRAINT SAMPLE_CART_ITEM_UK UNIQUE (USER_NO, PRODUCT_NO)
);


CREATE TABLE SAMPLE_ORDERS
(
	ORDER_NO number(4,0) NOT NULL,
	ORDER_CREATE_DATE date DEFAULT SYSDATE,
	ORDER_STATUS varchar2(20) DEFAULT '결재완료',
	TOTAL_ORDER_PRICE number(8) NOT NULL,
	USED_POINT number(8) NOT NULL,
	TOTAL_CREDIT_PRICE number(8) NOT NULL,
	DEPOSIT_POINT number(8) NOT NULL,
	USER_NO number(4,0) NOT NULL,
	PRIMARY KEY (ORDER_NO)
);


CREATE TABLE SAMPLE_ORDER_ITEMS
(
	ORDER_NO number(4,0) NOT NULL,
	PRODUCT_NO number(4,0) NOT NULL,
	PRODUCT_AMOUNT number(3,0),
	PRODUCT_PRICE number(8),
	CONSTRAINT ORDER_ITEM_UK UNIQUE (ORDER_NO, PRODUCT_NO)
);


CREATE TABLE SAMPLE_POINT_HISTORIES
(
	USER_NO number(4,0) NOT NULL,
	ORDER_NO number(4,0) NOT NULL,
	DEPOSIT_POINT number(8) NOT NULL,
	CURRENT_POINT number(8) NOT NULL,
	CREATE_DATE date DEFAULT SYSDATE,
	CONSTRAINT POINT_HISTORY_UK UNIQUE (USER_NO, ORDER_NO)
);


CREATE TABLE SAMPLE_PRODUCTS
(
	PRODUCT_NO number(4,0) NOT NULL,
	PRODUCT_NAME varchar2(100) NOT NULL,
	PRODUCT_MAKER varchar2(100) NOT NULL,
	PRODUCT_PRICE number(8) NOT NULL,
	PRODUCT_DISCOUNT_RATE number(2,2) DEFAULT 0.15,
	PRODUCT_STOCK number(3,0) DEFAULT 100,
	PRODUCT_CREATE_DATE date DEFAULT SYSDATE,
	PRIMARY KEY (PRODUCT_NO)
);


CREATE TABLE SAMPLE_USERS
(
	USER_NO number(4,0) NOT NULL,
	USER_ID varchar2(20) NOT NULL,
	USER_PASSWORD varchar2(20) NOT NULL,
	USER_NAME varchar2(20) NOT NULL,
	USER_POINT number(8) DEFAULT 0,
	USER_CREATE_DATE date DEFAULT SYSDATE,
	PRIMARY KEY (USER_NO)
);



/* Create Foreign Keys */

ALTER TABLE SAMPLE_ORDER_ITEMS
	ADD FOREIGN KEY (ORDER_NO)
	REFERENCES SAMPLE_ORDERS (ORDER_NO)
;


ALTER TABLE SAMPLE_POINT_HISTORIES
	ADD FOREIGN KEY (ORDER_NO)
	REFERENCES SAMPLE_ORDERS (ORDER_NO)
;


ALTER TABLE SAMPLE_CART_ITEMS
	ADD FOREIGN KEY (PRODUCT_NO)
	REFERENCES SAMPLE_PRODUCTS (PRODUCT_NO)
;


ALTER TABLE SAMPLE_ORDER_ITEMS
	ADD FOREIGN KEY (PRODUCT_NO)
	REFERENCES SAMPLE_PRODUCTS (PRODUCT_NO)
;


ALTER TABLE SAMPLE_CART_ITEMS
	ADD FOREIGN KEY (USER_NO)
	REFERENCES SAMPLE_USERS (USER_NO)
;


ALTER TABLE SAMPLE_ORDERS
	ADD FOREIGN KEY (USER_NO)
	REFERENCES SAMPLE_USERS (USER_NO)
;


ALTER TABLE SAMPLE_POINT_HISTORIES
	ADD FOREIGN KEY (USER_NO)
	REFERENCES SAMPLE_USERS (USER_NO)
;



