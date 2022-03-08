--hive/spark sql中hive对有null值的列进行avg,sum,count等操作时会过滤null值--
--hive标准sql进行null判断时不能使用逻辑运算符，必须使用is [not] null语句，因为null参数任何运算，返回false或null.

-- -orderId|customerName
-- |003|wang|
-- |008|lee|

-- -orderId|payId|cost
-- |003|p13|30.1
-- |005|p15|700.9

select count(1) as total,
 count(t_order.orderId) as order_count,
 count(t_payment.orderId) as payment_count,
 count(case when t_payment.orderId is not null and t_order.orderId is not null then 1 else null end) as joined_count
from t_order
full join t_payment
on t_order.orderId=t_payment.orderId