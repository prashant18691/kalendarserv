
### kalendarserv

A time-management and scheduling calendar service built with Spring Boot & JPA(Hibernate) & google calendar events sync.

The application is deployed at https://kalendarserv.herokuapp.com. 

## Steps to follow

1. Import the postman collections from https://www.getpostman.com/collections/1f925f58c382f92a9277

2. Operations

      *POST*              Register User            https://kalendarserv.herokuapp.com/users/register
      
      *POST*              Get Token                https://kalendarserv.herokuapp.com/authenticate
      
      *GET*               Get user by id           https://kalendarserv.herokuapp.com/users/id/{userid}
      
      *GET*               Get user by email        https://kalendarserv.herokuapp.com/users/email/{emailid}
      
      *POST*              Add slots for user       https://kalendarserv.herokuapp.com/slots/email/{emailid}
      
      *GET*               Find slots               https://kalendarserv.herokuapp.com/slots/email/{emailid}/date/{dd-MM-yyyy}
      
      *POST*              Book slot                https://kalendarserv.herokuapp.com/slots/{slotid}

