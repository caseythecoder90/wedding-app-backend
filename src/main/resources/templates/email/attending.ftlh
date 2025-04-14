<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Wedding RSVP Confirmation</title>
    <style>
        body { font-family: 'Arial', sans-serif; line-height: 1.6; color: #333; max-width: 600px; margin: 0 auto; }
        .header { text-align: center; padding: 20px; }
        .content { padding: 20px; }
        .footer { text-align: center; font-size; 12px; color: #777; padding: 20px; }
    </style>
</head>
<body>
    <div class="header">
        <h1> Thank you for your RSVP!</h1>
    </div>
    <div class="content">
        <p>Dear ${firstName} ${lastName},</p>

        <p>We are excited that you will be joining us on our special day! Your RSVP has been confirmed.</p>

        <h3>Your RSVP details:</h3>
        <ul>
            <li><strong>Status:</strong> Attending</li>
            <#if bringingPlusOne>
            <li><strong>Plus One:</strong> ${plusOneName!''}</li>
            </#if>
            <#if dietaryRestrictions?has_content>
            <li><strong>Dietary Restrictions:</strong> ${dietaryRestrictions}</li>
            </#if>
        </ul>

        <p>We look forward to celebrating with you!</p>

        <p>Best regards,<br>
        Casey and Yasmim</p>
    </div>
    <div class="footer">
        <p>If you need to edit your RSVP, you can search for your name at www.website.com and edit the form.</p>
    </div>
</body>
</html>