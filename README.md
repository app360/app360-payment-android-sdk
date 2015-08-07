#Introduction

App360 Payment SDK is a part of App360 Platform, it provides easiest way to integrate payment service (including sms, phone card and e-banking) into your application.

App360 Payment SDK use appscoped-id session of App360SDK to make payment. So, you must integrate with App360SDK first in order to call any Payment API.

The App360 Payment SDK supports Android version 4.0 and above.

#Getting started with Demo project

Firstly, clone or download this repository to your machine.

- `git clone https://github.com/app360/app360-payment-android-sdk.git`
- Or, download from https://github.com/app360/app360-payment-android-sdk/releases

Open `LoginActivity.java` inside demo project, find `App360SDK.initialize(...)` line and replace the placeholders with your application credentials.

Run the project. The app demonstrates capability of App360 SDK, including app-scoped ID and payment.

# 3 Step to integrate with App360 Payment SDK

## 1. Integrate with App360SDK

The first thing you need to do is integrate your game/app with App360SDK. For more information, please read [App360SDK document](https://github.com/app360/app360-android-sdk) to know how to do that.

## 2. Add App360 Payment SDK to your project

Download the Android SDK from the this repo, unzip the App360 Payment SDK file.

### Eclipse
- _File > Import..._, then inside the Import dialog, choose _General_ > _Existing projects into workspace_, then select _app360paymentsdk_ folder. 
- Repeat this step to import _app360sdk_.
- Right-click on _app360paymentsdk_ project, choose _Properties_, inside _Android_ > _Library_, add _app360sdk_ as a library of _app360paymentsdk_ .
- Right-click on your project, choose _Properties_, inside _Android_ > _Library_, add _app360paymentsdk_ as a library project dependency.
### Android studio
- Import App360 Payment SDK: Choose _File_ > _Import Module..._, then browse to app360paymentsdk directory inside the repository. Click Finish and wait until the import is completed.
- Import App360SDK: Choose _File_ > _Import Module..._, then browse to app360sdk directory inside the repository. Click Finish and wait until the import is completed.
- Setting Dependencies: 

    Open File > Project Structure, select _app360paymentsdk_ module, select Dependencies tab, press the plus (+) symbol in the top-right corner, select Module dependency, then select _app360sdk_.
 ![dependency app360paymentsdk](http://i.imgur.com/XcS1tda.png?1)
 
    Select your app module, select Dependencies tab, press the plus (+) symbol in the top-right corner, select Module dependency, then select _app360paymentsdk_.

    ![dependency your app](http://i.imgur.com/FxGInqj.png?1)

## 3. Integrate with Payment SDK

### 3.1. Payment flow

Before going to integrate with any API, you should look and know about payment flow of App360 Platform.

In order to secure payment flow, your application might choose to integrate with our SDK on both client-side and server-side, in which case the payment flow is depict in the following diagram:

![Payment flow](http://www.websequencediagrams.com/cgi-bin/cdraw?lz=dGl0bGUgUGF5bWVudCBzZXF1ZW5jZQoKR2FtZS0-U0RLOgAUCXJlcXVlc3QgKDEpClNESy0-R2FtZTogVHJhbnNhY3Rpb24gaWQsIHN0YXR1cywgYW1vdW50ICgyKQBGB0dhbWUgc2VydmVyOiBzZW5kIHQAMAtkYXRhIGZvciBpbnNwZQBJBigzAF8LADIJACoMaWQAYQgsIHVzZXJfaWQgKDQAbAYAZQcAgT8HYWNrIChIVFRQAIEaByAyMDApICg1AB0PAIEYDWNvbmZpcm0gKDYAExMAFAs3KQ&s=rose)

1. The application (client-side) calls payment API from the SDK, optionally with a custom payload (documented below).
2. The SDK returns transaction id and other details (if available)
3. The application client sends transaction data to its server awaiting confirmation
4. SDK server calls a pre-registered endpoint of the application server to notify about transaction status when it completes
    - Note that there is no guarantee about the order of (3) and (4) (i.e. (4) may happen before (3))
5. Application server acknowledges SDK server's call by responding with HTTP status code 200.
6. Application server validates the transaction based on the information it has (transaction ID, payload, etc.)
7. Application server confirms/notifies game client about the status of the transaction

**Note**:
- To register your application's server endpoint, go to https://developers.app360.vn/; set _Payment callback endpoint_ in application details page, _Information_ tab.
- Before using any payment methods, the application must first initialize the SDK. See section _SDK Initialization_ above.

### 3.2. Making SMS transaction

To make a sms transaction, client must call SDK API to request make transaction. After received request, SDK will call to SDK's server and return a list of available sms syntax. Client open SMS Application via Intent to make transaction with information is provided.
```Java
//Set payload string. The addition infomation you want to send with transaction. such as id of user make this transaction
//This payload string will send to callback URL (that configured on dashboard) when transaction finish successfully
String payload = "gamer-id";
SmsRequest smsRequest = new SmsRequest.Builder()
                            .setAmounts(amount) // required
                            .setSmsVendor("vendor") // required
                            .setPayload(payload) // optional
                            .setListener(new SmsRequestListener() {
                                @Override
                                public void onSuccess(SmsTransaction transaction) {
                                    // Send sms using transaction.getSyntax() and transaction.getRecipient()
                                }
                                @Override
                                public void onFailure(Throwable throwable) {	
                                    //API request failed				
                                }
                            })
                            .build();
smsRequest.execute();
```

### 3.3. Making card transaction

After user enter card (phone card or game card) information and hit a button to purchase, client call SDK API to make transaction with information above.

```Java
//Set payload string. The addition infomation you want to send with transaction. such as id of user make this transaction
//This payload string will send to callback URL (that configured on dashboard) when transaction finish successfully
String payload = "gamer-id";
CardRequest cardRequest = new CardRequest.Builder()
                .setCardCode("card code") // required
                .setCardSerial("card serial") // required
                .setCardVendor("vendor") // required
                .setPayload(payload) // optional
                .setSync(true) // optional, default is setSync(false)
                .setListener(new CardRequestListener() {

                    @Override
                    public void onSuccess(CardTransaction transaction) {
                        if (transaction.getStatus() == TransactionStatus.COMPLETED) {
                            // API request success
                        } else {
                            // API response 200 OK but something wrong
                        }
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        // API request failed
                    }
                }).build();
cardRequest.execute(); 
```
### 3.4. Making e-banking transaction

To make a e-banking transaction, client must call SDK API to request make transaction. After received request, SDK will call to SDK's server and return an url. Client open a web page that load url above and guide user make transaction on it.

```Java
//Set payload string. The addition infomation you want to send with transaction. such as id of user make this transaction
//This payload string will send to callback URL (that configured on dashboard) when transaction finish successfully
String payload = "gamer-id";
BankRequest bankRequest = new BankRequest.Builder()
                .setAmount(amount) // required
                .setPayload(payload) // optional
                .setListener(new BankRequestListener() {
                    @Override
                    public void onSuccess(BankTransaction transaction) {
                        // Open URL transaction.getPayUrl()
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        // Get URL Banking Payment fail
                    }
                })
                .build();
bankRequest.execute();
```


# What's next?

- Checkout [our document](http://docs.app360.vn/) for more infomation of App360SDK
- Integrate with [Payment API](http://docs.app360.vn/?page_id=271)
- If you got any trouble, checkout the [FAQ page](http://docs.app360.vn/?page_id=228) or send a support request

#Support
Please contact [us](mailto:support@app360.vn) for general inquiries.

##For a technical issue
In case you have a technical issue, you can reach [our technical support team](mailto:support@app360.vn).
Please provide the following information when you reach out, it'll allow us to help you much more quickly.

 - **The library version** you're using. You can get the precise number by
   printing the result of the `App360SDK.getVersion()` method.
 - **The platform** used to produce the problem (device model or simulator),
   and the Android version.
 - **The steps** to reproduce the problem.
 - If possible, **some pieces of code**, or even a project.

> For more information, please go to https://developers.app360.vn.
