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
                    <#if attending && dietaryRestrictions?has_content>
                    <tr>
                        <th>Dietary Restrictions:</th>
                        <td>${dietaryRestrictions}</td>
                    </tr>
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
            
            <#if isFamilyRsvp>
            <div class="rsvp-details">
                <h3>Group Details: ${familyGroupName}</h3>
                <#if familyMembers?? && familyMembers?size gt 0>
                    <p><strong>Additional Guests (${familyAttendingCount} of ${familyTotalCount} attending):</strong></p>
                    <table>
                        <thead>
                            <tr>
                                <th>Name</th>
                                <th>Age Group</th>
                                <th>Attending</th>
                                <th>Dietary Restrictions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <#list familyMembers as member>
                                <tr class="${member.isAttending?string('attending', 'not-attending')}">
                                    <td>${member.firstName} ${member.lastName}</td>
                                    <td>${member.ageGroup!""}</td>
                                    <td><strong>${member.isAttending?string('Yes', 'No')}</strong></td>
                                    <td>${member.dietaryRestrictions!""}</td>
                                </tr>
                            </#list>
                        </tbody>
                    </table>
                <#else>
                    <p><em>No additional guests in this group.</em></p>
                </#if>
            </div>
            </#if>
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
                                <th>Dietary Restrictions</th>
                                <th>Email</th>
                            </tr>
                        </thead>
                        <tbody>
                            <#list attendingRsvps as rsvp>
                                <tr class="attending">
                                    <td>${rsvp.guestName}</td>
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

                <!-- Additional Guests (Family Members) Attending -->
                <h3 class="section-title">Additional Guests Attending</h3>
                <p>Complete list of family members and plus-ones who are attending:</p>
                <table>
                    <thead>
                        <tr>
                            <th>Group Name</th>
                            <th>Guest Name</th>
                            <th>Age Group</th>
                            <th>Dietary Restrictions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <#-- This will be populated by the backend with family member data -->
                        <#if attendingFamilyMembers?? && attendingFamilyMembers?size gt 0>
                            <#list attendingFamilyMembers as member>
                                <tr class="attending">
                                    <td>${member.groupName}</td>
                                    <td>${member.firstName} ${member.lastName}</td>
                                    <td>${member.ageGroup!""}</td>
                                    <td>${member.dietaryRestrictions!""}</td>
                                </tr>
                            </#list>
                        <#else>
                            <tr>
                                <td colspan="4"><em>No additional guests attending yet.</em></td>
                            </tr>
                        </#if>
                    </tbody>
                </table>

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