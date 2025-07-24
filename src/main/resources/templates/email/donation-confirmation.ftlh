<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Donation Confirmation - Casey & Yasmim's Wedding</title>
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
            background: linear-gradient(135deg, #91C3E5 0%, #C9E4DE 100%);
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
        .donation-details {
            background-color: #f8f9fa;
            border-left: 4px solid #91C3E5;
            padding: 20px;
            margin: 25px 0;
            border-radius: 5px;
        }
        .donation-details h3 {
            margin: 0 0 15px 0;
            color: #2c3e50;
            font-size: 18px;
        }
        .detail-row {
            display: flex;
            justify-content: space-between;
            margin-bottom: 8px;
            padding: 5px 0;
            border-bottom: 1px solid #e9ecef;
        }
        .detail-row:last-child {
            border-bottom: none;
        }
        .detail-label {
            font-weight: bold;
            color: #495057;
        }
        .detail-value {
            color: #6c757d;
        }
        .amount {
            font-size: 20px;
            font-weight: bold;
            color: #28a745;
        }
        .next-steps {
            background-color: #e8f4fd;
            border-radius: 8px;
            padding: 20px;
            margin: 25px 0;
        }
        .next-steps h3 {
            margin: 0 0 15px 0;
            color: #0d47a1;
            font-size: 18px;
        }
        .next-steps ul {
            margin: 0;
            padding-left: 20px;
        }
        .next-steps li {
            margin-bottom: 8px;
            color: #1565c0;
        }
        .message-box {
            background-color: #fff3cd;
            border: 1px solid #ffeaa7;
            border-radius: 5px;
            padding: 15px;
            margin: 20px 0;
        }
        .message-box .message-label {
            font-weight: bold;
            color: #856404;
            margin-bottom: 5px;
        }
        .message-content {
            color: #856404;
            font-style: italic;
        }
        .signature {
            margin-top: 30px;
            padding-top: 20px;
            border-top: 2px solid #e9ecef;
        }
        .signature-text {
            font-size: 16px;
            color: #2c3e50;
            margin-bottom: 10px;
        }
        .couple-names {
            font-size: 20px;
            font-weight: bold;
            color: #91C3E5;
            margin-bottom: 5px;
        }
        .footer {
            background-color: #f8f9fa;
            padding: 20px;
            text-align: center;
            color: #6c757d;
            font-size: 12px;
        }
        .footer a {
            color: #91C3E5;
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
            .detail-row {
                flex-direction: column;
            }
            .detail-label {
                margin-bottom: 3px;
            }
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <span class="emoji">💕</span>
            <h1>Thank You for Your Donation!</h1>
        </div>

        <div class="content">
            <div class="greeting">
                Dear ${donorName},
            </div>

            <div class="main-message">
                We have received your generous donation to our honeymoon fund! Thank you so much for thinking of us and helping make our dream honeymoon a reality.
            </div>

            <div class="donation-details">
                <h3>Donation Details</h3>
                <div class="detail-row">
                    <span class="detail-label">Amount:</span>
                    <span class="detail-value amount">$${donationAmount}</span>
                </div>
                <div class="detail-row">
                    <span class="detail-label">Payment Method:</span>
                    <span class="detail-value">${paymentMethod}</span>
                </div>
                <div class="detail-row">
                    <span class="detail-label">Date:</span>
                    <span class="detail-value">${donationDate}</span>
                </div>
                <#if hasPaymentReference>
                <div class="detail-row">
                    <span class="detail-label">Reference:</span>
                    <span class="detail-value">${paymentReference}</span>
                </div>
                </#if>
                <div class="detail-row">
                    <span class="detail-label">Status:</span>
                    <span class="detail-value">${donationStatus}</span>
                </div>
            </div>

            <#if hasDonationMessage>
            <div class="message-box">
                <div class="message-label">Your Message:</div>
                <div class="message-content">"${donationMessage}"</div>
            </div>
            </#if>

            <div class="next-steps">
                <h3>What happens next? ✨</h3>
                <ul>
                    <li>We will confirm your payment once we receive it</li>
                    <li>You'll receive a personalized thank you note from us</li>
                    <li>We'll share photos and stories from our honeymoon adventure!</li>
                </ul>
            </div>

            <div class="main-message">
                Your kindness and generosity mean the world to us. We can't wait to create amazing memories on our honeymoon, knowing that friends and family like you helped make it possible!
            </div>

            <div class="signature">
                <div class="signature-text">With love and gratitude,</div>
                <div class="couple-names">Casey & Yasmim ❤️</div>
            </div>
        </div>

        <div class="footer">
            <p>This is an automated confirmation email for your honeymoon registry donation.</p>
            <p>Donation ID: ${donationId} | <#if hasPhone>Contact: ${donorPhone}<#else>Email: ${donorEmail}</#if></p>
        </div>
    </div>
</body>
</html>