#Akka Http Handle Rejections Example

- This template is about handling rejections in akka-http routes.
- A Rejection is not a failure, it means to pass a request to another branch in the routing tree in case if it does not match with a directive.
- So if a request does not match a filter directive, it is then considered as rejected.

This template shows two ways of handling the rejections in akka-http routes:
1. By explicitly defining the Rejection Handlers
2. By using an implicit Custom Rejection Handler

To test these Rejection Handlers, run the individual example apps and hit the specified endpoints(via postman or through curl) in a way that is not supported by our route so that the request gets rejected and handled by the rejection handler.

## Prerequisites

- Scala Build Tool(SBT), version 1.4.7
- Scala, version 2.12.12
- Akka-Http, version 10.1.7

