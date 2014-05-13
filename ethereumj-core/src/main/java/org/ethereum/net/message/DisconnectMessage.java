package org.ethereum.net.message;

import org.ethereum.net.rlp.RLPItem;
import org.ethereum.net.rlp.RLPList;
import org.ethereum.net.Command;
import static org.ethereum.net.Command.DISCONNECT;
import static org.ethereum.net.message.ReasonCode.DISCONNECT_REQUESTED;

/**
 * www.ethereumJ.com
 * User: Roman Mandeleil
 * Created on: 06/04/14 14:56
 */
public class DisconnectMessage extends Message {

    private ReasonCode reason;

    public DisconnectMessage(RLPList rawData) {
        super(rawData);
    }

    @Override
    public void parseRLP() {

        RLPList paramsList = (RLPList) rawData.getElement(0);

        if (Command.fromInt(((RLPItem)(paramsList).getElement(0)).getData()[0]) != DISCONNECT){
            throw new Error("Disconnect: parsing for mal data");
        }

        byte[] reasonB = ((RLPItem)paramsList.getElement(1)).getData();
        if (reasonB == null){
            this.reason = DISCONNECT_REQUESTED;
        } else {
            this.reason = ReasonCode.fromInt(reasonB[0]);
        }
        this.parsed = true;
        // todo: what to do when mal data ?
    }

    @Override
    public byte[] getPayload() {
        return null;
    }

    public ReasonCode getReason() {
        if (!parsed) parseRLP();
        return reason;
    }

    public String toString(){
        if (!parsed) parseRLP();
        return "Disconnect Message [ reason=" + reason + " ]";
    }
}
