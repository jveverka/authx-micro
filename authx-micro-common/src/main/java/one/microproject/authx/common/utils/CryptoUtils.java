package one.microproject.authx.common.utils;

import one.microproject.authx.common.dto.KeyPairData;
import one.microproject.authx.common.exceptions.CryptoProcessingException;
import one.microproject.authx.common.exceptions.DataProcessingException;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

import java.io.ByteArrayInputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public final class CryptoUtils {

    public static final String BC_PROVIDER = "BC";
    public static final String ALGORITHM = "RSA";
    public static final String X509_TYPE = "X.509";
    public static final String SHA256_RSA = "SHA256withRSA";
    public static final String CN_DIR_NAME = "CN=";

    private CryptoUtils() {
    }

    public static KeyPairData generateKeyPair(String id, String issuer, String subject, Instant notBefore, TimeUnit unit, Long duration) {
        try {
            Instant notAfter = Instant.ofEpochMilli(notBefore.toEpochMilli() + unit.toMillis(duration));
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM, BC_PROVIDER);
            SecureRandom secureRandom = SecureRandom.getInstance("NativePRNG");
            keyPairGenerator.initialize(2048, secureRandom);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            X509Certificate certificate = createSignedCertificate(issuer, subject, notBefore, notAfter, keyPair);
            return new KeyPairData(id, certificate, keyPair.getPrivate());
        } catch(Exception e) {
            throw new CryptoProcessingException(e);
        }
    }

    public static X509Certificate createSignedCertificate(String issuer, String subject, Instant notBefore, Instant notAfter, KeyPair keyPair) {
        try {
            X500Name x500issuer = new X500Name(CN_DIR_NAME + issuer);
            BigInteger serial = BigInteger.valueOf(System.currentTimeMillis());
            X500Name x500subject = new X500Name(CN_DIR_NAME + subject);
            SubjectPublicKeyInfo publicKeyInfo = SubjectPublicKeyInfo.getInstance(keyPair.getPublic().getEncoded());
            X509v3CertificateBuilder certBuilder = new X509v3CertificateBuilder(x500issuer, serial, Date.from(notBefore), Date.from(notAfter), x500subject, publicKeyInfo);
            JcaContentSignerBuilder jcaContentSignerBuilder = new JcaContentSignerBuilder(SHA256_RSA);
            ContentSigner signer = jcaContentSignerBuilder.build(keyPair.getPrivate());
            CertificateFactory certificateFactory = CertificateFactory.getInstance(X509_TYPE, BC_PROVIDER);
            byte[] certBytes = certBuilder.build(signer).getEncoded();
            return (X509Certificate) certificateFactory.generateCertificate(new ByteArrayInputStream(certBytes));
        } catch(Exception e) {
            throw new CryptoProcessingException(e);
        }
    }

    public static String serializeX509Certificate(X509Certificate certificate) throws CryptoProcessingException {
        try {
            return Base64.getEncoder().encodeToString(certificate.getEncoded());
        } catch(Exception e) {
            throw new CryptoProcessingException(e);
        }
    }

    public static X509Certificate deserializeX509Certificate(String base64EncodedCertificate) throws CryptoProcessingException {
        try {
            byte[] data = Base64.getDecoder().decode(base64EncodedCertificate);
            CertificateFactory certificateFactory = CertificateFactory.getInstance(X509_TYPE, BC_PROVIDER);
            return (X509Certificate) certificateFactory.generateCertificate(new ByteArrayInputStream(data));
        } catch(Exception e) {
            throw new CryptoProcessingException(e);
        }
    }

    public static String serializePrivateKey(PrivateKey privateKey) {
        return Base64.getEncoder().encodeToString(privateKey.getEncoded());
    }

    public static PrivateKey deserializePrivateKey(String base64EncodedCertificate) throws CryptoProcessingException {
        try {
            byte[] data = Base64.getDecoder().decode(base64EncodedCertificate);
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM, BC_PROVIDER);
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(data);
            return keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        } catch(Exception e) {
            throw new CryptoProcessingException(e);
        }
    }

    public static String getSha512HashBase64(String data) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] digest = md.digest(data.getBytes(StandardCharsets.UTF_8));
            return org.bouncycastle.util.encoders.Base64.toBase64String(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new DataProcessingException(e);
        }
    }

}
