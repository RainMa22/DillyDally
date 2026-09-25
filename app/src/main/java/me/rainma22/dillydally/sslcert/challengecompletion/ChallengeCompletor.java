package me.rainma22.dillydally.sslcert.challengecompletion;

import java.io.IOException;
import java.security.KeyPair;

import me.rainma22.dillydally.sslcert.OrderChallenge;

public interface ChallengeCompletor {

    AutoCloseable completeChallenge(OrderChallenge challenge, KeyPair kp) throws IOException;

}
