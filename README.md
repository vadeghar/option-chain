# option-chain

Spring Boot Project
  - Spring JPA
  - Spring Schedulers
  - MySQL

Project Flow: 
It's about Nifty and BankNifty Options 
1. Calculates the at the money strike price based on spot price
2. Saves the Open Interest & Change in OI for the next 4 strike prices for CE option strikes
3. Saves the Open Interest & Change in OI for the previous 4 strike prices for PE option strikes

Eg: 
1. NIFTY is at 16120, Calculate ATM strike as 16100
2. Saves Open interest data of 16100CE, 16150CE, 16200CE and 16250CE in Database
3. Saves Open interest data of 16100PE, 16050PE, 16000PE and 15950PE in Database

Based on OI data of nearest strikes, it can be easy to find the direction of market.

As of now there is no UI representation, Need to implement further.

