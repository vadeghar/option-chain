# option-chain

Spring Boot Project
  - Spring JPA
  - Spring Schedulers
  - MySQL

Project Flow: 
It's about Nifty and BankNifty Options open interest
1. Calculates the at the money strike price based on spot price
2. Saves the Open Interest & Change in OI for the next above and below 3 strike prices for CE option strikes
3. Saves the Open Interest & Change in OI for the next above and below 3 strike prices for PE option strikes
4. Graphical representation of change in OI for PE and CE side

Eg: 
1. NIFTY is at 16120, Calculate ATM strike as 16100 and Saves the CE data till 16250CE to 15950 
          16100 + (3 * 50) = 16250 and 16100 - (3*50) = 15950
2. Saves Open interest data of 15950CE to 16250CE
3. Similarly PE data of 15950PE to 16250PE

Based on OI data of nearest strikes, it can be easy to find the direction of market.


