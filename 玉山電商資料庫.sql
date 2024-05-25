create table [dbo].[Product] (
    [ProductID] nvarchar(50) primary key,
    [ProductName] nvarchar(100) NOT NULL,
    [Price] int NOT NULL,
    [Quantity] int NOT NULL
);

create table [dbo].[Order] (
    [OrderID] nvarchar(50) primary key,
    [MemberID] nvarchar(50) NOT NULL,
    [Price] int NOT NULL,
    [PayStatus] int NOT NULL
);

create table [dbo].[OrderDetail] (
    [OrderItemSN] int identity(1,1) primary key,
    [OrderID] nvarchar(50) NOT NULL,
    [ProductID] nvarchar(50) NOT NULL,
    [Quantity] int NOT NULL,
    [StandPrice] int NOT NULL,
    [ItemPrice] int NOT NULL,
    foreign key ([OrderID]) references [Order]([OrderID]),
    foreign key ([ProductID]) references [Product]([ProductID])
);


insert into [dbo].[Product] (ProductID, ProductName, Price, Quantity) values
('P001', 'osii 舒壓按摩椅', 98000, 5),
('P002', '網友最愛起司蛋糕', 1200, 50),
('P003', '真愛密碼項鍊', 8500, 20);

select * from [dbo].[Product]

insert into [dbo].[Order] (OrderID, MemberID, Price, PayStatus) values
('Ms20250801186230', '458', 98000, 1),
('Ms20250805157824', '55688', 9700, 0),
('Ms20250805258200', '1713', 2400, 1);

select * from [dbo].[Order]

insert into [dbo].[OrderDetail] (OrderID, ProductID, Quantity, StandPrice, ItemPrice) values
('Ms20250801186230', 'P001', 1, 98000, 98000),
('Ms20250805157824', 'P002', 1, 1200, 1200),
('Ms20250805157824', 'P003', 1, 8500, 8500),
('Ms20250805258200', 'P002', 2, 1200, 2400);
