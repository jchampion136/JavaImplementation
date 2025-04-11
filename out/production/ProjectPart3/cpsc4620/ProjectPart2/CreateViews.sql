/* Developed by Miguel Mejia And Jackson Champion*/
CREATE VIEW ToppingPopularity AS
SELECT
    topping_TopName AS Topping,
    CAST(
       COUNT(p.topping_TopID) + COUNT(CASE WHEN p.pizza_topping_IsDouble IS TRUE THEN 1 END)
            AS DECIMAL(5,2) ) AS ToppingCount
FROM 
    topping t
LEFT JOIN 
    pizza_topping p on p.topping_TopID = t.topping_TopID
GROUP BY
    topping_TopName
ORDER BY
    ToppingCount DESC,
    Topping;

CREATE VIEW ProfitByPizza AS
SELECT
    pizza_Size AS Size,
    pizza_CrustType AS Crust,
    SUM(p.pizza_CustPrice - p.pizza_BusPrice) AS Profit,
    DATE_FORMAT(ordertable_OrderDateTime, '%c/%Y') AS OrderMonth
FROM 
    pizza p
JOIN 
    ordertable o on p.ordertable_OrderID = o.ordertable_OrderID
GROUP BY
    pizza_Size,
    pizza_CrustType,
    OrderMonth
ORDER BY
    Profit;

CREATE VIEW ProfitByOrderType AS
SELECT
   o.ordertable_OrderType AS customerType,
   DATE_FORMAT(o.ordertable_OrderDateTime, '%c/%Y') AS OrderMonth,
   SUM(o.ordertable_CustPrice) AS TotalOrderPrice,
   SUM(o.ordertable_BusPrice) AS TotalOrderCost,
   SUM(o.ordertable_CustPrice - o.ordertable_BusPrice) AS Profit
FROM ordertable o
GROUP BY
    ordertable_OrderType,
    OrderMonth

UNION ALL

SELECT
   NULL AS customerType,
   'Grand Total' AS OrderMonth,
   SUM(o.ordertable_CustPrice),
   SUM(o.ordertable_BusPrice),
   SUM(o.ordertable_CustPrice - o.ordertable_BusPrice)
FROM ordertable o

ORDER BY
   OrderMonth,
   customerType,
   Profit DESC;