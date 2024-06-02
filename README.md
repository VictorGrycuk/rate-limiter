# Rate Limiter
We have a Notification system that sends out email notifications of various types (status update, daily news, project invitations, etc). We need to protect recipients from getting too many emails, either due to system errors or due to abuse, so let’s limit the number of emails sent to them by implementing a rate-limited version of NotificationService.

The system must reject requests that are over the limit.

Some sample notification types and rate limit rules, e.g.:

    Status: not more than 2 per minute for each recipient
    News: not more than 1 per day for each recipient
    Marketing: not more than 3 per hour for each recipient
    Etc. these are just samples, the system might have several rate limit rules!

## Infrastructure
[IDD](https://www.codurance.com/publications/2017/12/08/introducing-idd) was selected for the infrastructure of the project.
- The `action` package contains all the entry points to the system
- The `domain` package contains all the domain models
- The `infrastructure` contains the services unrelated to the domain itself

## The Rate Limiter
To allow flexibility when configuring current and new rate limiting configuration, two patterns were used:
- Chain of Responsibility
- Strategy

### Chain of Responsibility
While in the classic chain of responsibility each handle are tightly chained by making each handle call the next handle in the chain, I opted to remove the reference to the next handle in the chain.
This allows to add, remove, and order elements in the chain more easily than explicitly referencing the next handler.

### Strategy
While it started as a template method pattern, I opted for composition over inheritance since it is much easier to test. It ended up looking more like a strategy pattern than a template method.

## How to run it
After running the project in an IDE of your choice, the following endpoint will be available:
`POST http://localhost:8080/notification`.

The accepted body is the following:

```json
{
  "messageType": "STATUS",
  "message": "message",
  "userId": "c31d4206-2955-4098-a341-833349b8811a"
}
```

If the message has not been rate limited, a request will be printed to the console.