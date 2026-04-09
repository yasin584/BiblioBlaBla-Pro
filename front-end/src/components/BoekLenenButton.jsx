import { useNavigate } from 'react-router-dom';


const BoekLenenButton = () => {
    const navigate = useNavigate();

    const handlBoekLennen = () => {
        navigate('/lenenToevoegen');
    };

    return (
<button 
    onClick={handlBoekLennen}
    className=" w-full bg-gray-100 text-black px-4 py-2 border border-gray-300 rounded-md hover:bg-gray-200 cursor-pointer"
>
    Boek lenen
</button>
    );
}

export default BoekLenenButton;