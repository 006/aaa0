# [JSON Web Tokens (JWTs)](https://jwt.io/)

## What is JSON Web Token?

JSON Web Token (JWT) is an open standard (RFC 7519) that defines a compact and self-contained way for securely transmitting information between parties as a JSON object. This information can be verified and trusted because it is digitally signed. JWTs can be signed using a secret (with the HMAC algorithm) or a public/private key pair using RSA or ECDSA[^1].

## The JW*/JOSE (JavaScript Object Signing and Encryption) bucket

JWT (JSON Web Token) is a format for securely transmitting information as a JSON object, often used for authentication and authorization. JWS (JSON Web Signature) is a method to sign the JWT, ensuring its integrity and authenticity, while JWE (JSON Web Encryption) encrypts the JWT, ensuring its confidentiality.
![JW*](/assets/jw_.png)[^2]

### JSON Web Key (JWK)

### JSON Web Algorithm (JWA)

### JSON Web Encryption (JWE)

### JSON Web Signature (JWS)

### JSON Web Token (JWT) [RFC 7519](https://datatracker.ietf.org/doc/html/rfc7519)

JSON Web Token (JWT) is an open standard ([RFC 7519](https://datatracker.ietf.org/doc/html/rfc7519)) that defines a compact and self-contained way for securely transmitting information between parties as a JSON object. This information can be verified and trusted because it is digitally signed. JWTs can be signed using a secret (with the HMAC algorithm) or a public/private key pair using RSA or ECDSA.[^3]

[^1]: [Introduction to JSON Web Tokens](https://jwt.io/introduction)
[^2]: [Introduction to JWT (Also JWS, JWE, JWA, JWK)](https://codecurated.com/blog/introduction-to-jwt-jws-jwe-jwa-jwk/)
[^3]: [Introduction to JSON Web Tokens](https://jwt.io/introduction)
