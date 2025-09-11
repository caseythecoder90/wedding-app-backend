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
        <h1>Obrigado pelo seu RSVP!</h1>
        
        <!-- Engagement Photo -->
        <div style="margin: 20px 0; text-align: center;">
            <img src="https://res.cloudinary.com/dwdaehpml/image/upload/w_800,q_auto,f_auto/sitting-bench-landscape_hxe6tk"
                 alt="Foto de noivado do Casey e Yasmim - beijo romântico" 
                 style="max-width: 100%; height: auto; border-radius: 8px; box-shadow: 0 4px 8px rgba(0,0,0,0.1);">
        </div>
    </div>
    <div class="content">
        <p>Querido(a) ${firstName} ${lastName},</p>

        <p>Estamos muito animados que você estará conosco no nosso dia especial! Seu RSVP foi confirmado.</p>

        <h3>Detalhes do seu RSVP:</h3>
        <ul>
            <li><strong>Status:</strong> Comparecendo</li>
            <#if dietaryRestrictions?has_content>
            <li><strong>Restrições Alimentares:</strong> ${dietaryRestrictions}</li>
            </#if>
        </ul>

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
        </#if>

        <p>Estamos ansiosos para celebrar com você!</p>

        <!-- Wedding Information Section -->
        <div style="background-color: #f8f9fa; padding: 20px; border-radius: 8px; margin: 20px 0; border-left: 4px solid #5D8AA8;">
            <h3 style="color: #5D8AA8; margin-top: 0;">O que está disponível no nosso site</h3>
            <p style="margin-bottom: 15px;">Visite <strong>www.caseylovesyas.com</strong> para explorar:</p>
            <ul style="margin: 0; padding-left: 20px;">
                <li><strong>Informações do Resort:</strong> Detalhes sobre nosso lindo local de casamento e acomodações</li>
                <li><strong>Atividades Locais:</strong> Coisas para fazer e ver enquanto você estiver visitando</li>
                <li><strong>Lista de Lua de Mel:</strong> Nos ajude a criar memórias inesquecíveis com uma contribuição para nossa lua de mel</li>
                <li><strong>Galeria de Fotos:</strong> Navegue pelas nossas fotos de noivado e preparações do casamento</li>
                <li><strong>Informações de Viagem:</strong> Opções de transporte e recomendações</li>
                <li><strong>Perguntas Frequentes:</strong> Respostas para perguntas comuns sobre o casamento</li>
            </ul>
            <p style="margin-top: 15px; margin-bottom: 0;"><em>Tudo que você precisa saber sobre nosso dia especial está a apenas um clique de distância!</em></p>
        </div>

        <p>Com carinho,<br>
        Casey e Yasmim</p>
    </div>
    <div class="footer">
        <p>Se você precisar editar seu RSVP, pode pesquisar usando seu código de convite (na página de RSVP) em www.caseylovesyas.com e editar o formulário.</p>
    </div>
</body>
</html>