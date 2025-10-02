import jwt from 'jsonwebtoken';

interface TokenDto{
    username:string;
    authority:string;
}

const key = 'holafjkdshjgjkfdhghreughuriehgbnurtebgurebhguyred';

export const jwtService = {
    generar(data:TokenDto):string{
        return jwt.sign(data, key, {
            expiresIn:'1h',
            subject:data.username
        });
    },
    validation(token:string):TokenDto{
        
        return jwt.verify(token, key) as TokenDto;
    }
}