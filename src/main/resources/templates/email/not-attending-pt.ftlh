<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Confirmação de RSVP do Casamento</title>
    <style>
        body { font-family: 'Arial', sans-serif; line-height: 1.6; color: #333; max-width: 600px; margin: 0 auto; }
        .header { text-align: center; padding: 20px; }
        .content { padding: 20px; }
        .footer { text-align: center; font-size; 12px; color: #777; padding: 20px; }
    </style>
</head>
<body>
    <div class="header">
        <h1>Seu RSVP foi recebido</h1>
        
        <!-- Engagement Photo -->
        <div style="margin: 20px 0; text-align: center;">
            <img src="https://res.cloudinary.com/dwdaehpml/image/upload/w_800,q_auto,f_auto/sitting-bench-landscape_hxe6tk"
                 alt="Foto de noivado do Casey e Yasmim" 
                 style="max-width: 100%; height: auto; border-radius: 8px; box-shadow: 0 4px 8px rgba(0,0,0,0.1);">
        </div>
    </div>
    <div class="content">
        <p>Querido(a) ${firstName} ${lastName},</p>

        <p>Obrigado por responder ao nosso convite de casamento. Recebemos seu RSVP indicando que você não poderá comparecer.</p>

        <#if isFamilyRsvp>
        <h3>Seu Grupo: ${familyGroupName}</h3>
        <p>Você também confirmou presença para seus convidados:</p>
        
        <#if familyMembers?? && familyMembers?size gt 0>
            <div style="margin-left: 20px;">
                <p><strong>Convidados adicionais (${familyAttendingCount} de ${familyTotalCount} comparecendo):</strong></p>
                <ul>
                    <#list familyMembers as member>
                        <li>
                            <strong>${member.firstName} ${member.lastName}</strong>
                            <#if member.ageGroup?has_content>(${member.ageGroup})</#if>
                            - <em>${member.isAttending?string('Comparecendo', 'Não comparecendo')}</em>
                            <#if member.isAttending && member.dietaryRestrictions?has_content>
                                <br><small>Restrições alimentares: ${member.dietaryRestrictions}</small>
                            </#if>
                        </li>
                    </#list>
                </ul>
            </div>
        <#else>
            <p><em>Nenhum convidado adicional neste RSVP.</em></p>
        </#if>
        
        <#if familyAttendingCount gt 0>
        <p>Estamos ansiosos para celebrar com ${familyAttendingCount} convidado(s) que estarão presentes!</p>
        <#else>
        <p>Embora sentiremos sua falta no nosso dia especial, entendemos que nem todos podem comparecer. Agradecemos por nos informar.</p>
        </#if>
        <#else>
        <p>Embora sentiremos sua falta no nosso dia especial, entendemos que nem todos podem comparecer. Agradecemos por nos informar.</p>
        </#if>

        <!-- Wedding Information Section -->
        <div style="background-color: #f8f9fa; padding: 20px; border-radius: 8px; margin: 20px 0; border-left: 4px solid #5D8AA8;">
            <h3 style="color: #5D8AA8; margin-top: 0;">Mantenha-se conectado com nossa jornada de casamento</h3>
            <p style="margin-bottom: 15px;">Mesmo que você não possa estar conosco pessoalmente, ainda pode fazer parte do nosso dia especial! Visite <strong>www.caseylovesyas.com</strong> para:</p>
            <ul style="margin: 0; padding-left: 20px;">
                <li><strong>Galeria de Fotos:</strong> Navegue pelas nossas fotos de noivado e veja atualizações do casamento</li>
                <li><strong>Lista de Lua de Mel:</strong> Nos ajude a criar memórias inesquecíveis com uma contribuição para nossa lua de mel</li>
                <li><strong>Detalhes do Casamento:</strong> Saiba mais sobre nosso lindo local e celebração</li>
                <li><strong>Nossa História:</strong> Leia sobre nossa jornada juntos</li>
            </ul>
            <p style="margin-top: 15px; margin-bottom: 0;"><em>Adoraríamos compartilhar nossa alegria com você à distância!</em></p>
        </div>

        <p>Com carinho,<br>
        Casey e Yasmim</p>
    </div>
    <div class="footer">
        <p>Se seus planos mudarem e você precisar editar seu RSVP, pode pesquisar usando seu código de convite (na página de RSVP) em www.caseylovesyas.com e editar o formulário.</p>
    </div>
</body>
</html>