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
        
        <!-- Engagement Photo -->
        <div style="margin: 20px 0; text-align: center;">
            <img src="https://res.cloudinary.com/dwdaehpml/image/upload/w_800,q_auto,f_auto/sitting-bench-landscape_hxe6tk"
                 alt="Casey and Yasmim engagement photo - romantic kiss" 
                 style="max-width: 100%; height: auto; border-radius: 8px; box-shadow: 0 4px 8px rgba(0,0,0,0.1);">
        </div>
    </div>
    <div class="content">
        <p>Dear ${firstName} ${lastName},</p>

        <p>We are excited that you will be joining us on our special day! Your RSVP has been confirmed.</p>

        <h3>Your RSVP details:</h3>
        <ul>
            <li><strong>Status:</strong> Attending</li>
            <#if dietaryRestrictions?has_content>
            <li><strong>Dietary Restrictions:</strong> ${dietaryRestrictions}</li>
            </#if>
        </ul>

        <#if isFamilyRsvp>
        <h3>Your Group: ${familyGroupName}</h3>
        <p>You have also RSVP'd for your guests:</p>
        
        <#if familyMembers?? && familyMembers?size gt 0>
            <div style="margin-left: 20px;">
                <p><strong>Additional guests (${familyAttendingCount} of ${familyTotalCount} attending):</strong></p>
                <ul>
                    <#list familyMembers as member>
                        <li>
                            <strong>${member.firstName} ${member.lastName}</strong>
                            <#if member.ageGroup?has_content>(${member.ageGroup})</#if>
                            - <em>${member.isAttending?string('Attending', 'Not attending')}</em>
                            <#if member.isAttending && member.dietaryRestrictions?has_content>
                                <br><small>Dietary restrictions: ${member.dietaryRestrictions}</small>
                            </#if>
                        </li>
                    </#list>
                </ul>
            </div>
        <#else>
            <p><em>No additional guests in this RSVP.</em></p>
        </#if>
        </#if>

        <p>We look forward to celebrating with you!</p>

        <!-- Wedding Information Section -->
        <div style="background-color: #f8f9fa; padding: 20px; border-radius: 8px; margin: 20px 0; border-left: 4px solid #5D8AA8;">
            <h3 style="color: #5D8AA8; margin-top: 0;">What's Available on Our Website</h3>
            <p style="margin-bottom: 15px;">Visit <strong>www.caseylovesyas.com</strong> to explore:</p>
            <ul style="margin: 0; padding-left: 20px;">
                <li><strong>Resort Information:</strong> Details about our beautiful wedding venue and accommodations</li>
                <li><strong>Local Activities:</strong> Things to do and see while you're visiting</li>
                <li><strong>Honeymoon Registry:</strong> Help us create unforgettable memories with a contribution to our honeymoon</li>
                <li><strong>Photo Gallery:</strong> Browse through our engagement photos and wedding preparations</li>
                <li><strong>Travel Information:</strong> Transportation options and recommendations</li>
                <li><strong>FAQ:</strong> Answers to commonly asked questions about the wedding</li>
            </ul>
            <p style="margin-top: 15px; margin-bottom: 0;"><em>Everything you need to know for our special day is just a click away!</em></p>
        </div>

        <p>Best regards,<br>
        Casey and Yasmim</p>
    </div>
    <div class="footer">
        <p>If you need to edit your RSVP, you can search using your invitation code (on RSVP page) at www.caseylovesyas.com and edit the form.</p>
    </div>
</body>
</html>