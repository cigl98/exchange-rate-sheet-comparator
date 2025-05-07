# Exchange rate sheet comparator

- Implements comparison of exchange rates from CNB (https://www.cnb.cz/cs/financni_trhy/devizovy_trh/kurzy_devizoveho_trhu/denni_kurz.xml) and Currency-api (https://github.com/fawazahmed0/exchange-api)

**Requirements**

 - Maven 3+
 - Java 21+

  **Environment variables**
  - USER_USERNAME - sets login username (default _user_)
  - USER_PASSWORD - sets login password (default _password_)
 
  **Endpoints**
  - */api/v1/rates-compare?currencyPair={currencyPair}* - currencyPair is in format [currency1/currency2], must be URL encoded. e.g.: http://localhost/ap1/v1/rates-compare?currencyPair=eur%2Fczk
  - */api/v1/allowed-pairs* - returns allowed currency pairs
  - */actuator/health* - healthcheck endpoint

  - */api/v1/* endpoints are secured by HTTP Basic auth, user must login with credentials set in environment variables above 
