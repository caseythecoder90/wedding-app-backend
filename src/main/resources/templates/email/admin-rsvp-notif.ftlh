<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Wedding RSVP Update</title>
    <style>
        body {
            font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;
            line-height: 1.6;
            color: #333;
            max-width: 800px;
            margin: 0 auto;
            padding: 20px;
        }
        .header {
            background-color: #5D8AA8;
            color: white;
            padding: 20px;
            text-align: center;
            border-radius: 5px 5px 0 0;
        }
        .content {
            padding: 20px;
            border: 1px solid #ddd;
            border-top: none;
            border-radius: 0 0 5px 5px;
        }
        .section {
            margin-bottom: 30px;
        }
        .section-title {
            border-bottom: 2px solid #5D8AA8;
            padding-bottom: 5px;
            margin-top: 25px;
            color: #5D8AA8;
        }
        .rsvp-details, .summary-stats {
            background-color: #f8f9fa;
            padding: 15px;
            border-radius: 5px;
            margin: 15px 0;
        }
        .stat-box {
            display: inline-block;
            background-color: #5D8AA8;
            color: white;
            padding: 15px;
            border-radius: 5px;
            margin: 10px;
            min-width: 100px;
            text-align: center;
        }
        .stat-number {
            font-size: 24px;
            font-weight: bold;
            margin-bottom: 5px;
        }
        .stat-label {
            font-size: 14px;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 20px;
        }
        th, td {
            padding: 8px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }
        th {
            background-color: #f2f2f2;
        }
        .attending {
            background-color: #e8f5e9;
        }
        .not-attending {
            background-color: #fbe9e7;
        }
        .update-info {
            font-size: 12px;
            color: #666;
            text-align: right;
            margin-top: 20px;
        }
        .footer {
            margin-top: 20px;
            text-align: center;
            color: #777;
            font-size: 12px;
        }
    </style>
</head>
<body>
    <div class="header">
        <h1>Wedding RSVP Update</h1>
    </div>
    <div class="content">
        <!-- Latest RSVP Information -->
        <div class="section">
            <h2 class="section-title">New RSVP Submitted</h2>
            <p>A new RSVP has been submitted by <strong>${firstName} ${lastName}</strong>:</p>

            <div class="rsvp-details">
                <table>
                    <tr>
                        <th>Guest Name:</th>
                        <td>${firstName} ${lastName}</td>
                    </tr>
                    <tr>
                        <th>Email:</th>
                        <td>${guestEmail!""}</td>
                    </tr>
                    <tr>
                        <th>Attending:</th>
                        <td><strong>${attending?string('Yes', 'No')}</strong></td>
                    </tr>
                    <#if attending>
                    <tr>
                        <th>Bringing Plus One:</th>
                        <td>${bringingPlusOne?string('Yes', 'No')}</td>
                    </tr>
                    <#if bringingPlusOne && plusOneName?has_content>
                    <tr>
                        <th>Plus One Name:</th>
                        <td>${plusOneName}</td>
                    </tr>
                    </#if>
                    <#if dietaryRestrictions?has_content>
                    <tr>
                        <th>Dietary Restrictions:</th>
                        <td>${dietaryRestrictions}</td>
                    </tr>
                    </#if>
                    </#if>
                    <tr>
                        <th>RSVP ID:</th>
                        <td>${rsvpId}</td>
                    </tr>
                    <#if submissionDate?has_content>
                    <tr>
                        <th>Submitted On:</th>
                        <td>${submissionDate}</td>
                    </tr>
                    </#if>
                </table>
            </div>
        </div>

        <!-- RSVP Summary Section -->
        <#if summaryError?? && summaryError>
            <div class="section">
                <h2 class="section-title">RSVP Summary</h2>
                <p>Error retrieving RSVP summary: ${errorMessage!""}</p>
            </div>
        <#else>
            <div class="section">
                <h2 class="section-title">Current RSVP Summary</h2>

                <!-- Stats Overview -->
                <div class="summary-stats">
                    <div class="stat-box">
                        <div class="stat-number">${totalRsvps}</div>
                        <div class="stat-label">Total RSVPs</div>
                    </div>
                    <div class="stat-box">
                        <div class="stat-number">${totalAttending}</div>
                        <div class="stat-label">Attending</div>
                    </div>
                    <div class="stat-box">
                        <div class="stat-number">${totalNotAttending}</div>
                        <div class="stat-label">Not Attending</div>
                    </div>
                    <div class="stat-box">
                        <div class="stat-number">${totalGuests}</div>
                        <div class="stat-label">Total Guests</div>
                    </div>
                </div>

                <!-- Attending Guests -->
                <h3 class="section-title">Guests Attending (${totalAttending})</h3>
                <#if attendingRsvps?? && attendingRsvps?size gt 0>
                    <table>
                        <thead>
                            <tr>
                                <th>Guest Name</th>
                                <th>Plus One</th>
                                <th>Plus One Name</th>
                                <th>Dietary Restrictions</th>
                                <th>Email</th>
                            </tr>
                        </thead>
                        <tbody>
                            <#list attendingRsvps as rsvp>
                                <tr class="attending">
                                    <td>${rsvp.guestName}</td>
                                    <td>${rsvp.bringingPlusOne?string('Yes', 'No')}</td>
                                    <td>${rsvp.plusOneName!""}</td>
                                    <td>${rsvp.dietaryRestrictions!""}</td>
                                    <td>${(rsvp.guestEmail?? && rsvp.guestEmail != "")?then(rsvp.guestEmail, "Not provided")}</td>
                                </tr>
                            </#list>
                        </tbody>
                    </table>
                <#else>
                    <p>No guests have confirmed attendance yet.</p>
                </#if>

                <!-- Not Attending Guests -->
                <h3 class="section-title">Guests Not Attending (${totalNotAttending})</h3>
                <#if notAttendingRsvps?? && notAttendingRsvps?size gt 0>
                    <table>
                        <thead>
                            <tr>
                                <th>Guest Name</th>
                                <th>Email</th>
                            </tr>
                        </thead>
                        <tbody>
                            <#list notAttendingRsvps as rsvp>
                                <tr class="not-attending">
                                    <td>${rsvp.guestName}</td>
                                    <td>${(rsvp.guestEmail?? && rsvp.guestEmail != "")?then(rsvp.guestEmail, "Not provided")}</td>
                                </tr>
                            </#list>
                        </tbody>
                    </table>
                <#else>
                    <p>No guests have declined yet.</p>
                </#if>

                <div class="update-info">
                    Last updated: ${lastUpdated!""}
                </div>
            </div>
        </#if>
    </div>
    <div class="footer">
        <p>This is an automated notification from your Wedding RSVP system.</p>
    </div>
</body>
</html>