package edu.ap.spring.controller;

import edu.ap.spring.service.Block;
import edu.ap.spring.service.BlockChain;
import edu.ap.spring.service.Wallet;
import edu.ap.spring.transaction.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

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

//                Block block = new Block();
//        block.setPreviousHash(bChain.getLastHash());
//
//        try{
//            block.addTransaction(walletA.sendFunds(walletB.getPublicKey(), 30f), bChain);
//        } catch (Exception e){
//
//        }
//
//        bChain.addBlock(block);


        System.out.println(walletA.getBalance());
        System.out.println(walletB.getBalance());
    }

    @GetMapping("/")
    public String index(){
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String home(){
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

//        System.out.println(balance);

        return "balancecheck";
    }

    @GetMapping("/transaction")
    public String getTransactionForm(Model model){
        return "transactionform";
    }

    @PostMapping("/transaction")
    public String doTransaction(@RequestParam("wallet1") String wfrom,
                                @RequestParam("wallet2") String wto,
                                @RequestParam("amount") String wamount){
        System.out.println("dopost");
        Wallet from;
        Wallet to;
        float amount = Float.parseFloat(wamount);

        if(wfrom == "walletA"){
            from = walletA;
            to = walletB;
        } else{
            from = walletB;
            to = walletA;
        }

        System.out.println("A " + walletA.getBalance());
        System.out.println("B " +  walletB.getBalance());
        System.out.println("amount" + amount);

        Block block = new Block();
        block.setPreviousHash(bChain.getLastHash());

        try{
            block.addTransaction(to.sendFunds(from.getPublicKey(), amount), bChain);
        } catch (Exception e){

        }

        bChain.addBlock(block);

        System.out.println("A " + walletA.getBalance());
        System.out.println("B " + walletB.getBalance());

        return "redirect:/home";
    }

}
