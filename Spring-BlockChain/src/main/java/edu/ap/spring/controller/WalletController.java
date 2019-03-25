package edu.ap.spring.controller;

import edu.ap.spring.service.Block;
import edu.ap.spring.service.BlockChain;
import edu.ap.spring.service.Wallet;
import edu.ap.spring.transaction.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.PostConstruct;

@Controller
@ComponentScan
public class WalletController {

    @Autowired
    private BlockChain bChain;
    @Autowired
    private Wallet coinbase, walletA, walletB;
    private Transaction genesisTransaction;

    @PostConstruct
    public void init(){
//        System.out.println("entered init");
        bChain.setSecurity();
        coinbase.generateKeyPair();
        walletA.generateKeyPair();
        walletB.generateKeyPair();

        //create genesis transaction, which sends 100 coins to walletA:
        genesisTransaction = new Transaction(coinbase.getPublicKey(), walletA.getPublicKey(), 100f);
        genesisTransaction.generateSignature(coinbase.getPrivateKey());	 // manually sign the genesis transaction
        genesisTransaction.transactionId = "0"; // manually set the transaction id
        Block genesis = new Block();
        genesis.setPreviousHash("0");
        genesis.addTransaction(genesisTransaction, bChain);
        bChain.addBlock(genesis);

//        System.out.println(walletA.getBalance());
//        System.out.println(walletB.getBalance());
    }

    @GetMapping("/")
    public String index(){
        return "index";
    }

    @GetMapping("/balance/{wallet}")
    public String checkBalanceOfWallet(@PathVariable("wallet") String walletstr, Model model){
        Wallet wallet;

        if(walletstr.equals("walletA")){
            wallet = walletA;
        } else{
            wallet = walletB;
        }

        float balance = wallet.getBalance();

        model.addAttribute("balance", balance);

        System.out.println(balance);

        return "balancecheck";
    }

    @GetMapping(/transaction)
    public String getTransactionForm(){
        return "transaction";
    }

}
