# Authentication and Authorization with Auth0

## Preface

While browsing X recently, I came across "Auth0" and, intrigued, looked into it.  It's a SaaS product focused on **OAuth 2.0** specifications and flows, which brought back memories of working on a Telematics project for Electric Vehicles in China.  Back then, I had to build our entire stack on **Apache Oltu**, a framework that's sadly become obsolete.

Checking out Auth0's website, I saw all the familiar OAuth 2.0 concepts were still there, but with one key difference: **JWT (JSON Web Tokens)**.

**[JWT](./jwt.md)** has been a game-changer in the OAuth 2.0 ecosystem.  It significantly simplifies access token verification for any resource server, drastically reducing the amount of code required.  It's a testament to how the OAuth landscape has evolved.

## Introduction

Early in my career in China, I focused on building interfaces for web and desktop applications. Though REST had emerged in 2000, we primarily used SOAP, RPC, and custom binary protocols. Recognizing the potential of REST combined with the newly released OAuth 2.0 in 2012, I advocated for adopting them as our next-generation API solution.

At that time, over a decade ago, OAuth 2.0 libraries for resource servers were scarce. Consequently, I built a custom implementation based on RFC 6749. Our **access tokens** were simple, short strings, requiring us to store associated metadata like client IDs, scopes, and expiration times in a database. Each API request necessitated database lookups for token verification.

Today, with **[JSON Web Tokens (JWTs)](./jwt.md)**, this process is significantly streamlined. JWTs embed necessary authorization information directly within the token, eliminating the need for constant database queries. This allows for offline verification, relying solely on the resource server's private key, making API security more efficient.

**[Auth0](https://auth0.com/)** is a platform that simplifies the process of implementing authentication and authorization in applications. It essentially provides "authentication as a service." Auth0 streamlines authentication and authorization, eliminating the need for custom implementations. This allows development teams to accelerate their development cycles and concentrate on core business innovations.

## Where to use Auth0

### Clients/APPs/Frontends

Examples are inside **[client](/client)** folder

### Resource Servers/APIs/Backends

Examples are inside **[server](/server)** folder
