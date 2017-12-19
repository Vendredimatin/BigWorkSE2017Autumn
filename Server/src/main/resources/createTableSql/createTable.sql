这个现在只是复制用的草稿

drop table MemberPromotion;
drop table TotalPromotion;
drop table CombinePromotion;

create table MemberPromotion(
  dayId integer,

  createTime bigint,
  lastModifiedTime bigint,
  beginTime bigint,
  endTime bigint,

  requiredLevel integer,
  discountFraction double,
  tokenAmount double
);


create table TotalPromotion(
  dayId integer,

  createTime bigint,
  lastModifiedTime bigint,
  beginTime bigint,
  endTime bigint,

  requiredTotal double,
  tokenAmount double,
  gifts varchar(1000)
);



create table CombinePromotion(
  dayId integer,

  createTime bigint,
  lastModifiedTime bigint,
  beginTime bigint,
  endTime bigint,

  discountAmount double,
  goodsCombination varchar(1000)
);

create table SalesSellReceipt(
  dayId integer,
  operatorId integer,
  createTime bigint,
  lastModifiedTime bigint,
  receiptState integer,

  clientId integer,
  clerkName varchar(30),
  stockName varchar(30),
  goodsList varchar(1000),
  discountAmount double,
  tokenAmount double,
  originSum double,
  comment varchar(150)
);


create table account(
    ID int auto increment primary key,
    name varchar(255),
    balance double
);


create table PaymentBillReceipt(
    dayId integer,
    operatorId integer,
    createTime bigint,
    lastModifiedTime bigint,
    receiptState integer,

    clerkName varchar(30),
    clientId integer,
    transferList varchar(1000),
    sum double
);

create table ChargeBillReceipt(
    dayId integer,
    operatorId integer,
    createTime bigint,
    lastModifiedTime bigint,
    receiptState integer,

    clerkName varchar(30),
    clientId integer,
    transferList varchar(1000),
    sum double
);

create table CashBillReceipt(
    dayId integer,
    operatorId integer,
    createTime bigint,
    lastModifiedTime bigint,
    receiptState integer,

    clerkName varchar(30),
    accountID INTEGER,
    total double;
    itemList varchar(1000);

);


create table SalesDetail(
    date bigint,
    goodsName varchar(30),
    goodsID integer,
    number integer,
    price double,
    total double,

    clientID integer,
    clerkName varchar(30)
    stockID integer
)


create table BusinessCondition(
    income double,
    expense double
)