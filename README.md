This Project introduces the function implementation of the **distributed session**.

> **this project related to following article [Mboot](https://mboot.herokuapp.com/post/56)**
 

Usually, our projects are deployed in multiple instances, and the production environment usually deploys four or five servers per module.

We know that after a user logs in, `session information needs to be stored`. Session information is usually stored in the server's memory and cannot be persisted (server restart failure), 

and multiple servers cannot coexist. In order to solve this problem, **we can store the session in a place shared by several servers**, such as Redis, as long as it is in an intranet, several servers can share Redis (Redis is also installed in a server).

## How to achieve it? Here is a brief description:

After the user logs in successfully, a random unique string is generated through the UUID, named token, and a value is set in redis, the key is the token string, and the value is the serialized string of the user object.

When a user visits another page and requests a method, check whether the request parameter or cookie has a token
    - If there is, query the token from redis to verify whether the token is valid
    - If not, throw an exception "User is not logged in"

 

Regarding parameter verification, here can be resolved through SpringMVC's `resolveArgument` method, that is, all method parameters will verify whether the user name is logged in during parameter verification. There is no need to write a paragraph in each method to check whether the user name is logged in, which is too redundant.

 
The following is the specific implementation. The code is posted from top to bottom (important to secondary). 

**This project related to following article [Mboot](https://mboot.herokuapp.com/post/56)**
