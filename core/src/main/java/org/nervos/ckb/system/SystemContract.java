package org.nervos.ckb.system;

import java.io.IOException;
import org.nervos.ckb.service.Api;
import org.nervos.ckb.system.type.SystemScriptCell;
import org.nervos.ckb.type.Block;
import org.nervos.ckb.type.OutPoint;

public class SystemContract {

  public static SystemScriptCell getSystemScriptCell(Api api) throws IOException {
    Block block = api.getBlockByNumber("0x0");
    if (block == null) {
      throw new IOException("Genesis block not found");
    }
    if (block.getTransactions().size() < 2) {
      throw new IOException("Genesis block transactions system script not found");
    }
    return new SystemScriptCell(
        block.getTransactions().get(0).getOutputs().get(1).getType().computeHash(),
        new OutPoint(block.getTransactions().get(1).getHash(), "0x0"));
  }
}
