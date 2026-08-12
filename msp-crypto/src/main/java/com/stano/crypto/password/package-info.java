/**
 * Provider-agnostic abstractions for one-way password hashing and verification.
 *
 * <p>Defines the {@link com.stano.crypto.password.Password} value type and the {@link
 * com.stano.crypto.password.PasswordEncryptionServices} contract, together with factories for
 * obtaining the platform's configured implementation without depending on a specific hashing
 * algorithm.
 */
package com.stano.crypto.password;
