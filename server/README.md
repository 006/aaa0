# How to verify a JWT

The validation process for JSON Web Tokens (JWTs) is defined in [RFC 7519, section 7.2](https://datatracker.ietf.org/doc/html/rfc7519#section-7.2). While you can implement your own validator following these steps, using a well-established library is generally more efficient.

Auth0 signs its JWTs with an X.509 certificate's public/private key pair. Therefore, retrieving the public key is the essential first step in the verification process. There are two primary methods for obtaining this public key, which are detailed below.

## Via well known jwks.json

## Via Management API
