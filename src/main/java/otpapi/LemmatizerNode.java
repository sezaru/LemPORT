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
    private static final String SECRET_COOKIE = "nodiff";

    private OtpErlangPid _lastPid;
    private final Lemmatizer _lemmatizer;
    private final OtpMbox _otpMbox;

    public static void main(String[] args) {
        final LemmatizerNode lemmatizerNode;

        try {
            lemmatizerNode = new LemmatizerNode(LemmatizerNode.NODE_NAME + "@" + "127.0.0.1", 9000);

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


    public LemmatizerNode(String node, int port) throws ParserConfigurationException, WordRankingLoadException, SAXException, DictionaryLoadException, IOException {
        super(node, SECRET_COOKIE, port);
        System.out.println("Node: " + node);

        _otpMbox = createMbox(NODE_MAILBOX);
        _lemmatizer = new Lemmatizer();
    }

    public final void performOneMessage() {
        try {
            System.out.println("Ready!");
            final OtpErlangTuple tuple = (OtpErlangTuple) _otpMbox.receive();

            System.out.println("Received new message");

            _lastPid = (OtpErlangPid) tuple.elementAt(0);
            final OtpErlangAtom dispatchName = (OtpErlangAtom) tuple.elementAt(1);

            System.out.println("Type: " + dispatchName.toString());

            switch (dispatchName.toString()) {
                case LEMMATIZE_DISPATCH:
                    final OtpErlangBinary token = (OtpErlangBinary) tuple.elementAt(2);
                    final OtpErlangBinary tag = (OtpErlangBinary) tuple.elementAt(3);
                    final String tokenString = new String(token.binaryValue());
                    final String tagString = new String(tag.binaryValue());
                    final String lemma = _lemmatizer.lemmatize(tokenString, tagString);
                    System.out.println(new StringBuilder().append("Lemma(").append(tagString).append(") ").append(tokenString).append(" = ").append(lemma).toString());

                    _otpMbox.send(_lastPid, createLemmaResultTuple(createLemmaResultsArray(tokenString, tagString, lemma)));
                    break;
                default:
                    break;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private OtpErlangList createLemmaResultTuple(OtpErlangObject[] lemmatizeResults) {
        return new OtpErlangList(lemmatizeResults);
    }

    private OtpErlangObject[] createLemmaResultsArray(String tokenString, String tagString, String lemma) {
        return new OtpErlangObject[] {
                new OtpErlangString(lemma),
                new OtpErlangString(tokenString),
                new OtpErlangString(tagString)
        };
    }
}
