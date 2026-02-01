import PreviewPanel from "../components/PreviewPanel";
import EditPanel from "../components/EditPanel";
import CreatePanel from "../components/CreatePanel";
import FileExplorer from "../components/FileExplorer";
import CramMode from "../components/CramMode";
import { useState } from "react";

export default function Explorer() {
    const [selectedMode, setSelectedMode] = useState("preview");
    const [selectedItem, setSelectedItem] = useState("Root Folder");
    const [selectedType, setSelectedType] = useState("root");

    function handleSelect(item, type, mode) {
        setSelectedMode(mode);
        setSelectedItem(item);
        setSelectedType(type);
    }

    function handleCramMode() {
        setSelectedMode("cram");
    }

    function handleCreateMode() {
        setSelectedMode("create");
    }

    function handleEditMode() {
        setSelectedMode("edit");
    }

    return (   
            <div>
                {selectedMode === "preview" && <PreviewPanel item={selectedItem} type={selectedType} onPlay={handleCramMode} onCreate={handleCreateMode} onEdit={handleEditMode}/>} 
                {selectedMode === "edit" && <EditPanel item={selectedItem} type={selectedType} />} 
                {selectedMode === "create" && <CreatePanel item={selectedItem} type={selectedType} />} 
                {selectedMode === "cram" && <CramMode />} 
                <FileExplorer onSelectItem={handleSelect} />
            </div>
    );
}