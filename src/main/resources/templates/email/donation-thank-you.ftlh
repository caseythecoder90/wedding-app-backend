<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Thank You - Casey & Yasmim's Wedding</title>
    <style>
        body {
            margin: 0;
            padding: 0;
            font-family: 'Georgia', 'Times New Roman', serif;
            line-height: 1.6;
            color: #333;
            background-color: #f9f9f9;
        }
        .container {
            max-width: 600px;
            margin: 0 auto;
            background-color: #ffffff;
            border-radius: 10px;
            overflow: hidden;
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
        }
        .header {
            background: linear-gradient(135deg, #FFC2AE 0%, #C9E4DE 100%);
            color: white;
            padding: 30px 20px;
            text-align: center;
        }
        .header h1 {
            margin: 0;
            font-size: 28px;
            font-weight: 300;
            text-shadow: 0 1px 3px rgba(0, 0, 0, 0.2);
        }
        .header .emoji {
            font-size: 36px;
            margin-bottom: 10px;
            display: block;
        }
        .content {
            padding: 30px 25px;
        }
        .greeting {
            font-size: 18px;
            color: #2c3e50;
            margin-bottom: 20px;
        }
        .main-message {
            font-size: 16px;
            margin-bottom: 25px;
            color: #555;
        }
        .highlight-box {
            background: linear-gradient(135deg, #fff5f5 0%, #f0f9ff 100%);
            border: 2px solid #FFC2AE;
            border-radius: 10px;
            padding: 25px;
            margin: 25px 0;
            text-align: center;
        }
        .highlight-box .amount {
            font-size: 24px;
            font-weight: bold;
            color: #d63384;
            margin-bottom: 10px;
        }
        .highlight-box .thank-you-text {
            font-size: 18px;
            color: #2c3e50;
            font-style: italic;
        }
        .message-box {
            background-color: #e8f5e8;
            border-left: 4px solid #28a745;
            border-radius: 5px;
            padding: 20px;
            margin: 25px 0;
        }
        .message-box .message-label {
            font-weight: bold;
            color: #155724;
            margin-bottom: 10px;
            font-size: 16px;
        }
        .message-content {
            color: #155724;
            font-style: italic;
            font-size: 15px;
        }
        .gratitude-section {
            background-color: #fff3cd;
            border-radius: 8px;
            padding: 25px;
            margin: 25px 0;
            text-align: center;
        }
        .gratitude-section h3 {
            margin: 0 0 15px 0;
            color: #856404;
            font-size: 20px;
        }
        .gratitude-section p {
            color: #856404;
            margin: 0;
            font-size: 16px;
        }
        .memories-section {
            background-color: #f8f9fa;
            border-radius: 8px;
            padding: 20px;
            margin: 25px 0;
        }
        .memories-section p {
            margin: 0 0 15px 0;
            color: #495057;
        }
        .memories-section p:last-child {
            margin-bottom: 0;
        }
        .signature {
            margin-top: 30px;
            padding-top: 20px;
            border-top: 2px solid #e9ecef;
            text-align: center;
        }
        .signature-text {
            font-size: 16px;
            color: #2c3e50;
            margin-bottom: 10px;
        }
        .couple-names {
            font-size: 22px;
            font-weight: bold;
            color: #FFC2AE;
            margin-bottom: 10px;
        }
        .ps-text {
            font-size: 14px;
            color: #6c757d;
            font-style: italic;
            margin-top: 15px;
        }
        .footer {
            background-color: #f8f9fa;
            padding: 20px;
            text-align: center;
            color: #6c757d;
            font-size: 12px;
        }
        .footer a {
            color: #FFC2AE;
            text-decoration: none;
        }
        @media (max-width: 600px) {
            .container {
                margin: 0 10px;
            }
            .header {
                padding: 20px 15px;
            }
            .header h1 {
                font-size: 24px;
            }
            .content {
                padding: 20px 15px;
            }
            .highlight-box .amount {
                font-size: 20px;
            }
            .highlight-box .thank-you-text {
                font-size: 16px;
            }
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <span class="emoji">🌟</span>
            <h1>Thank You for Your Generous Gift!</h1>
        </div>

        <div class="content">
            <div class="greeting">
                Dear ${donorName},
            </div>

            <div class="highlight-box">
                <div class="amount">$${donationAmount}</div>
                <div class="thank-you-text">Your generous contribution to our honeymoon fund has been confirmed!</div>
            </div>

            <div class="main-message">
                Thank you so much for your thoughtful and generous contribution to our honeymoon fund. Your kindness means the world to us, and we are truly grateful to have you in our lives.
            </div>

            <#if hasDonationMessage>
            <div class="message-box">
                <div class="message-label">You wrote to us:</div>
                <div class="message-content">"${donationMessage}"</div>
            </div>
            </#if>

            <div class="gratitude-section">
                <h3>We are so grateful! 💕</h3>
                <p>Having friends and family like you who are helping make our dream honeymoon a reality fills our hearts with joy. We cannot wait to share our adventures with you when we return!</p>
            </div>

            <div class="memories-section">
                <p>Your gift will help us create memories that will last a lifetime. From romantic dinners overlooking beautiful sunsets to exciting adventures exploring new places, every moment will be made more special knowing it was made possible by your kindness.</p>

                <p>We promise to think of you and all our loved ones as we embark on this new chapter of our lives together. Your support means everything to us!</p>
            </div>

            <div class="signature">
                <div class="signature-text">With love and endless gratitude,</div>
                <div class="couple-names">Casey & Yasmim ❤️</div>
                <div class="ps-text">P.S. We will be sure to send you photos from our trip! 📸✈️</div>
            </div>
        </div>

        <div class="footer">
            <p>Thank you for contributing to Casey & Yasmim's honeymoon fund!</p>
            <p>Donation confirmed on ${confirmedDate} | Original donation: ${donationDate}</p>
        </div>
    </div>
</body>
</html>