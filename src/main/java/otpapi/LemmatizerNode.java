package otpapi;
import com.ericsson.otp.erlang.*;
import dictionary.DictionaryLoadException;
import lemma.Lemmatizer;
import org.xml.sax.SAXException;
import rank.WordRankingLoadException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class LemmatizerNode extends OtpNode {
    private static final String LEMMATIZE_DISPATCH = "lemmatize";
    private static final String NODE_NAME = "lemport";
    private static final String NODE_MAILBOX = NODE_NAME + "-mailbox";

    private OtpErlangPid _lastPid;
    private final Lemmatizer _lemmatizer;
    private final OtpMbox _otpMbox;

    public static void main(String[] args) {
        final LemmatizerNode lemmatizerNode;

        try {
            lemmatizerNode = new LemmatizerNode(LemmatizerNode.NODE_NAME);

            while (true) {
                lemmatizerNode.performOneMessage();
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (WordRankingLoadException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (DictionaryLoadException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public LemmatizerNode(String node) throws ParserConfigurationException, WordRankingLoadException, SAXException, DictionaryLoadException, IOException {
        super(node);
        _otpMbox = createMbox(NODE_MAILBOX);
        _lemmatizer = new Lemmatizer();
    }

    public final void performOneMessage() {
        try {
            final OtpErlangTuple tuple = (OtpErlangTuple) _otpMbox.receive();

            _lastPid = (OtpErlangPid) tuple.elementAt(0);
            final OtpErlangAtom dispatchName = (OtpErlangAtom) tuple.elementAt(1);

            switch (dispatchName.toString()) {
                case LEMMATIZE_DISPATCH:
                    final OtpErlangBinary token = (OtpErlangBinary) tuple.elementAt(2);
                    final OtpErlangBinary tag = (OtpErlangBinary) tuple.elementAt(3);
                    final String tokenString = new String(token.binaryValue());
                    final String tagString = new String(tag.binaryValue());
                    final String lemma = _lemmatizer.lemmatize(tokenString, tagString);

                    _otpMbox.send(_lastPid, new OtpErlangString(lemma));
                    break;
                default:
                    break;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}