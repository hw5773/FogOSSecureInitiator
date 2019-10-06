package app.fogos;

import FlexID.FlexID;
import FlexID.FlexIDFactory;
import FlexID.InterfaceType;
import FlexID.Locator;
import FogOSSecurity.Role;
import FogOSSecurity.SecureFlexIDSession;
import com.sun.corba.se.impl.protocol.giopmsgheaders.LocateReplyOrReplyMessage;

import java.util.logging.Level;

public class Main {
    private static final String TAG = "FogOSSecureInitiator";

    public static void main(String[] args) {
	    FlexIDFactory factory = new FlexIDFactory();
	    String addr = "127.0.0.1";
	    int port = 5555;
	    Locator loc = new Locator(InterfaceType.ETH, addr, port);
	    byte[] peerID = {
                (byte) 0xd0, (byte) 0x93, (byte) 0xb9, (byte) 0xf6, (byte) 0xff, (byte) 0x83, (byte) 0xac, (byte) 0x89,
                (byte) 0xd9, (byte) 0x54, (byte) 0xb3, (byte) 0x03, (byte) 0x5d, (byte) 0x1f, (byte) 0x06, (byte) 0x47,
                (byte) 0xc1, (byte) 0x43, (byte) 0xb6, (byte) 0xed
        };

        byte[] masterKey = { 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0,
                0x1, 0x1, 0x1, 0x1, 0x1, 0x1, 0x1, 0x1,
                0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2,
                0x3, 0x3, 0x3, 0x3, 0x3, 0x3, 0x3, 0x3,
        };

	    FlexID id = factory.generateDeviceID();
	    FlexID peer = new FlexID(peerID);
	    peer.setLocator(loc);

        System.out.println("Identity");
        System.out.println("{ " + byteArrayToHex(peer.getIdentity()) + " }");

        java.util.logging.Logger.getLogger(TAG).log(Level.INFO, "Start: SecureInitiator");
	    SecureFlexIDSession secureFlexIDSession = new SecureFlexIDSession(Role.INITIATOR, id, peer);
        java.util.logging.Logger.getLogger(TAG).log(Level.INFO, "After initializing SecureFlexIDSession");
	    secureFlexIDSession.doHandshake();

        // TODO: Remove this Test Code when the implementation for the handshake is complete.
	    secureFlexIDSession.getSecurityParameters().setMasterSecret(masterKey);

	    // Send the test message
	    secureFlexIDSession.send("Hello");
    }

    static String byteArrayToHex(byte[] a) {
        int idx = 0;
        StringBuilder sb = new StringBuilder();
        for (final byte b: a) {
            sb.append(String.format("0x%02x, ", b & 0xff));
            idx++;
            if (idx % 8 == 0)
                sb.append("\n");
        }
        return sb.toString();
    }
}
