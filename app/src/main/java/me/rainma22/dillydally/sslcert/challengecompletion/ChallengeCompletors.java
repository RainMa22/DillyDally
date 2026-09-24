package me.rainma22.dillydally.sslcert.challengecompletion;

import me.rainma22.dillydally.conf.ConfBean;
import me.rainma22.dillydally.conf.SSLCertificateConfBean;
import me.rainma22.dillydally.exceptions.UnsupportedCompletorTypeException;

public class ChallengeCompletors {

    public static ChallengeCompletor fromConf(ConfBean conf)
            throws UnsupportedCompletorTypeException {
        SSLCertificateConfBean httpChallengeConfBean = conf.getSslCertificateConf();
        switch (httpChallengeConfBean.getType().toLowerCase()) {
            case "file":
                return fileBased(httpChallengeConfBean);
            default:
                throw new UnsupportedCompletorTypeException(
                        "unsupported challenge type configured: "
                                + httpChallengeConfBean.getType());
        }
    }

    public static FileBasedCompletor fileBased(SSLCertificateConfBean httpChallengeConfBean) {
        return new FileBasedCompletor(httpChallengeConfBean);
    }
}
